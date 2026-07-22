package com.LionKing.Teamply.domain.auth.repository;

import com.LionKing.Teamply.domain.auth.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    // 가장 최근에 발급된, 아직 사용되지 않은 인증 코드를 찾음
    Optional<PasswordResetToken> findTopByEmailAndCodeAndUsedFalseOrderByIdDesc(String email, String code);
}