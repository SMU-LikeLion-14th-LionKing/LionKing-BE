package com.LionKing.Teamply.domain.user.exception;

import com.LionKing.Teamply.global.apiPayload.code.BaseErrorCode;
import com.LionKing.Teamply.global.apiPayload.exception.handler.CustomException;

public class UserException extends CustomException {
    public UserException(BaseErrorCode code) {
        super(code);
    }
}