package com.LionKing.Teamply.domain.project.exception;

import com.LionKing.Teamply.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ProjectErrorCode implements BaseErrorCode {

    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "PROJECT404", "존재하지 않는 프로젝트입니다."),
    PROJECT_FORBIDDEN(HttpStatus.FORBIDDEN, "PROJECT403", "해당 프로젝트에 대한 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
