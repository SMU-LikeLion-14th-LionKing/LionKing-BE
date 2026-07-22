package com.LionKing.Teamply.domain.poll.exception;

import com.LionKing.Teamply.global.apiPayload.exception.handler.CustomException;

public class PollException extends CustomException {
    public PollException(PollErrorCode errorCode) {
        super(errorCode);
    }
}
