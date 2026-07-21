package com.LionKing.Teamply.domain.meeting.exception;

import com.LionKing.Teamply.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MeetingErrorCode implements BaseErrorCode {

    MEETING_NOT_FOUND(HttpStatus.NOT_FOUND, "MEETING404", "존재하지 않는 회의록입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
