package com.LionKing.Teamply.domain.projects.exception;

import com.LionKing.Teamply.global.apiPayload.exception.handler.CustomException;

public class ProjectException extends CustomException {
    public ProjectException(ProjectErrorCode errorCode) {
        super(errorCode);
    }
}
