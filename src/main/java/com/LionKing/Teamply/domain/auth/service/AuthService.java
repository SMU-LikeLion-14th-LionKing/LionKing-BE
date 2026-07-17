package com.LionKing.Teamply.domain.auth.service;

import com.LionKing.Teamply.domain.auth.dto.request.LoginRequest;
import com.LionKing.Teamply.domain.auth.dto.request.SignupRequest;
import com.LionKing.Teamply.domain.auth.dto.response.SignupResponse;
import com.LionKing.Teamply.domain.auth.dto.response.TokenResponse;
import com.LionKing.Teamply.domain.user.entity.User;
import com.LionKing.Teamply.domain.user.repository.UserRepository;
import com.LionKing.Teamply.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입
     */
    @Transactional
    public SignupResponse signup(SignupRequest request) {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 암호화 후 저장
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
     * 로그인 → Access/Refresh 토큰 발급
     */
    public TokenResponse login(LoginRequest request) {
        // 이메일로 사용자 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        // 토큰 발급
        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId(), user.getEmail());

        log.info("로그인 성공: userId={}, email={}", user.getId(), user.getEmail());
        return TokenResponse.of(user.getId(), accessToken, refreshToken);
    }

    /**
     * Refresh Token으로 Access Token 재발급
     */
    public TokenResponse reissue(String refreshToken) {
        // 토큰 유효성 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 Refresh Token 입니다.");
        }
        // Refresh Token 타입 검증
        if (!jwtTokenProvider.isRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Refresh Token이 아닙니다.");
        }

        Long userId = jwtTokenProvider.getUserId(refreshToken);
        String email = jwtTokenProvider.getEmail(refreshToken);

        // 사용자 존재 여부 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 새 토큰 발급
        String newAccessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getEmail());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getId(), user.getEmail());

        log.info("토큰 재발급 완료: userId={}, email={}", userId, email);
        return TokenResponse.of(user.getId(), newAccessToken, newRefreshToken);
    }
}