package com.LionKing.Teamply.domain.ai.exception;

import com.LionKing.Teamply.global.apiPayload.exception.handler.CustomException;

public class AiException extends CustomException {
    public AiException(AiErrorCode errorCode) {
        super(errorCode);
    }
}
