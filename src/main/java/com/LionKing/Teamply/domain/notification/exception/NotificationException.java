package com.LionKing.Teamply.domain.notification.exception;

import com.LionKing.Teamply.global.apiPayload.code.BaseErrorCode;
import com.LionKing.Teamply.global.apiPayload.exception.handler.CustomException;

public class NotificationException extends CustomException {
    public NotificationException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
