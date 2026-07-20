package com.LionKing.Teamply.global.apiPayload.code;

import com.LionKing.Teamply.global.common.ApiResponse;
import org.springframework.http.HttpStatus;

public interface BaseErrorCode {

    HttpStatus getHttpStatus();
    String getCode();
    String getMessage();

    default ApiResponse<Void> getErrorResponse() {
        return ApiResponse.fail(getCode(), getMessage());
    }
}