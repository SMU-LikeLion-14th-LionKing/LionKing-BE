package com.LionKing.Teamply.domain.comment.exception;

import com.LionKing.Teamply.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommentErrorCode implements BaseErrorCode {

    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT404_1", "존재하지 않거나 이미 삭제된 댓글입니다."),
    COMMENT_FORBIDDEN(HttpStatus.FORBIDDEN, "COMMENT403_1", "본인이 작성한 댓글만 수정할 수 있습니다."),
    COMMENT_CONTENT_REQUIRED(HttpStatus.BAD_REQUEST, "COMMENT400_1", "댓글 내용을 입력해주세요."),
    COMMENT_UPDATE_CONTENT_REQUIRED(HttpStatus.BAD_REQUEST, "COMMENT400_2", "수정할 댓글 내용을 입력해주세요."),
    INVALID_PAGE_PARAM(HttpStatus.BAD_REQUEST, "COMMENT400_3", "잘못된 페이징 파라미터입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}