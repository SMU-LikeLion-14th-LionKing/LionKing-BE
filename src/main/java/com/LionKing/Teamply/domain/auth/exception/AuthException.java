package com.LionKing.Teamply.domain.auth.exception;

import com.LionKing.Teamply.global.apiPayload.code.BaseErrorCode;
import com.LionKing.Teamply.global.apiPayload.exception.handler.CustomException;

public class AuthException extends CustomException {
    public AuthException(BaseErrorCode code) {
        super(code);
    }
}