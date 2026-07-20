package com.LionKing.Teamply.domain.post.exception;

import com.LionKing.Teamply.global.apiPayload.exception.handler.CustomException;

public class PostException extends CustomException {
    public PostException(PostErrorCode errorCode) {
        super(errorCode);
    }
}
