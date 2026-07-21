package com.LionKing.Teamply.domain.auth.exception;

import com.LionKing.Teamply.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {

    USER_NOT_FOUND_BY_EMAIL(HttpStatus.NOT_FOUND, "AUTH404", "가입되지 않은 이메일입니다."),
    INVALID_VERIFICATION_CODE(HttpStatus.UNAUTHORIZED, "AUTH401_1", "인증 코드가 일치하지 않거나 만료되었습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}