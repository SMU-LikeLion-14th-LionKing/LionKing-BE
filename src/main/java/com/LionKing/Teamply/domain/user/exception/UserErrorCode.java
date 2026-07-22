package com.LionKing.Teamply.domain.user.exception;

import com.LionKing.Teamply.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements BaseErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER404", "존재하지 않는 사용자 정보입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "USER400_1", "기존 비밀번호가 일치하지 않거나, 새 비밀번호 형식이 올바르지 않습니다."),
    INVALID_PAGING_PARAMETER(HttpStatus.BAD_REQUEST, "USER400_2", "잘못된 페이징 파라미터입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}