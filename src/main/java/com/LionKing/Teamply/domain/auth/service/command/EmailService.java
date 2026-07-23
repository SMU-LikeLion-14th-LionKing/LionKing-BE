package com.LionKing.Teamply.domain.auth.service.command;

import com.LionKing.Teamply.domain.auth.entity.PasswordResetToken;
import com.LionKing.Teamply.domain.auth.exception.AuthErrorCode;
import com.LionKing.Teamply.domain.auth.exception.AuthException;
import com.LionKing.Teamply.domain.auth.repository.PasswordResetTokenRepository;
import com.LionKing.Teamply.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    /**
     * 비밀번호 재설정을 위한 이메일 인증 코드 발송
     */
    @Transactional
    public void sendVerificationCode(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new AuthException(AuthErrorCode.USER_NOT_FOUND_BY_EMAIL);
        }

        String code = generateCode();
        
        // 3분 만료
        PasswordResetToken token = PasswordResetToken.builder()
                .email(email)
                .code(code)
                .expiryDate(LocalDateTime.now().plusMinutes(3))
                .build();
        
        passwordResetTokenRepository.save(token);

        sendEmail(email, "[Teamply] 비밀번호 재설정 인증 코드", "인증 코드: " + code + "\n\n코드는 3분간 유효합니다.");
        log.info("이메일 인증 코드 발송 완료: email={}, code={}", email, code);
    }

    /**
     * 인증 코드 단순히 맞는지 검증 (프론트에서 코드만 먼저 확인할 때 사용)
     */
    @Transactional(readOnly = true)
    public void verifyCode(String email, String code) {
        PasswordResetToken token = passwordResetTokenRepository
                .findTopByEmailAndCodeAndUsedFalseOrderByIdDesc(email, code)
                .orElseThrow(() -> new AuthException(AuthErrorCode.INVALID_VERIFICATION_CODE));

        if (token.isExpired()) {
            throw new AuthException(AuthErrorCode.INVALID_VERIFICATION_CODE);
        }
    }

    private void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("이메일 전송 실패: to={}", to, e);
            throw new RuntimeException("이메일 전송에 실패했습니다.", e);
        }
    }

    private String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 100000 ~ 999999
        return String.valueOf(code);
    }
}
