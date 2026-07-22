package com.LionKing.Teamply.domain.auth.service.command;

import com.LionKing.Teamply.domain.auth.dto.request.LoginRequest;
import com.LionKing.Teamply.domain.auth.dto.request.PasswordResetRequest;
import com.LionKing.Teamply.domain.auth.dto.request.SignupRequest;
import com.LionKing.Teamply.domain.auth.dto.response.SignupResponse;
import com.LionKing.Teamply.domain.auth.dto.response.TokenResponse;
import com.LionKing.Teamply.domain.auth.entity.PasswordResetToken;
import com.LionKing.Teamply.domain.auth.exception.AuthErrorCode;
import com.LionKing.Teamply.domain.auth.exception.AuthException;
import com.LionKing.Teamply.domain.auth.repository.PasswordResetTokenRepository;
import com.LionKing.Teamply.domain.user.entity.User;
import com.LionKing.Teamply.domain.user.repository.UserRepository;
import com.LionKing.Teamply.global.jwt.JwtTokenProvider;
import com.LionKing.Teamply.global.jwt.RefreshTokenRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthCommandService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    /**
     * 회원가입
     */
    @Transactional
    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .build();

        User savedUser = userRepository.save(user);
        log.info("회원가입 완료: email={}", user.getEmail());

        return SignupResponse.from(savedUser);
    }

    /**
     * 로그인 → Access/Refresh 토큰 발급 + Refresh Token Redis 저장
     */
    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId(), user.getEmail());

        refreshTokenRedisRepository.save(user.getId(), refreshToken, refreshTokenExpiration);

        log.info("로그인 성공: userId={}, email={}", user.getId(), user.getEmail());
        return TokenResponse.of(user.getId(), accessToken, refreshToken);
    }

    /**
     * Refresh Token으로 Access Token 재발급.
     * Redis에 저장된 값과 일치하는지 확인해 탈취/재사용을 방지, 재발급 시 토큰을 회전
     */
    @Transactional
    public TokenResponse reissue(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 Refresh Token 입니다.");
        }
        if (!jwtTokenProvider.isRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Refresh Token이 아닙니다.");
        }

        Long userId = jwtTokenProvider.getUserId(refreshToken);

        String savedRefreshToken = refreshTokenRedisRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("로그아웃되었거나 만료된 세션입니다. 다시 로그인해주세요."));

        if (!savedRefreshToken.equals(refreshToken)) {
            refreshTokenRedisRepository.deleteByUserId(userId);
            throw new IllegalArgumentException("유효하지 않은 Refresh Token 입니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        String newAccessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getEmail());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getId(), user.getEmail());

        refreshTokenRedisRepository.save(user.getId(), newRefreshToken, refreshTokenExpiration);

        log.info("토큰 재발급 완료: userId={}, email={}", userId, user.getEmail());
        return TokenResponse.of(user.getId(), newAccessToken, newRefreshToken);
    }

    /**
     * 로그아웃: Redis에 저장된 Refresh Token을 제거해 재발급을 막음.
     * (Access Token은 만료될 때까지는 유효하다는 점에 유의)
     */
    @Transactional
    public void logout(Long userId) {
        refreshTokenRedisRepository.deleteByUserId(userId);
    }

    /**
     * 로그인 전 비밀번호 재설정: 이메일 + 인증코드 검증 후 비밀번호 변경
     */
    @Transactional
    public void resetPasswordBeforeLogin(PasswordResetRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND_BY_EMAIL));

        PasswordResetToken token = passwordResetTokenRepository
                .findTopByEmailAndCodeAndUsedFalseOrderByIdDesc(request.getEmail(), request.getVerificationCode())
                .orElseThrow(() -> new AuthException(AuthErrorCode.INVALID_VERIFICATION_CODE));

        if (token.isExpired()) {
            throw new AuthException(AuthErrorCode.INVALID_VERIFICATION_CODE);
        }

        user.changePassword(passwordEncoder.encode(request.getNewPassword()));
        token.markUsed();
    }
}