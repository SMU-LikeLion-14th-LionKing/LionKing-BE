package com.LionKing.Teamply.domain.project.exception;

import com.LionKing.Teamply.global.apiPayload.code.BaseErrorCode;
import com.LionKing.Teamply.global.apiPayload.exception.handler.CustomException;

public class ProjectException extends CustomException {
    public ProjectException(BaseErrorCode code) {
        super(code);
    }
}
