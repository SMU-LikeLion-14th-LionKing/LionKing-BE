package com.LionKing.Teamply.domain.meeting.exception;

import com.LionKing.Teamply.global.apiPayload.code.BaseErrorCode;
import com.LionKing.Teamply.global.apiPayload.exception.handler.CustomException;

public class MeetingException extends CustomException {
    public MeetingException(BaseErrorCode code) {
        super(code);
    }
}
