package com.LionKing.Teamply.domain.post.exception;

import com.LionKing.Teamply.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PostErrorCode implements BaseErrorCode {

    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST404_1", "게시글을 찾을 수 없습니다."),
    POST_FORBIDDEN(HttpStatus.FORBIDDEN, "POST403_1", "게시글에 대한 권한이 없습니다."),
    POST_INVALID_TYPE(HttpStatus.BAD_REQUEST, "POST400_1", "유효하지 않은 게시글 타입입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
