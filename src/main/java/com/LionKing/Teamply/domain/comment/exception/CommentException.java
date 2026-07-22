package com.LionKing.Teamply.domain.comment.exception;

import com.LionKing.Teamply.global.apiPayload.exception.handler.CustomException;

public class CommentException extends CustomException {
    public CommentException(CommentErrorCode errorCode) {
        super(errorCode);
    }
}