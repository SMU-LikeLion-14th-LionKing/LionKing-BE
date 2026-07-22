package com.LionKing.Teamply.domain.poll.exception;

import com.LionKing.Teamply.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PollErrorCode implements BaseErrorCode {

    POLL_NOT_FOUND(HttpStatus.NOT_FOUND, "POLL404_1", "투표를 찾을 수 없습니다."),
    POLL_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "POLL404_2", "투표 선택지를 찾을 수 없습니다."),
    POLL_CLOSED(HttpStatus.BAD_REQUEST, "POLL400_1", "이미 마감된 투표입니다."),
    POLL_FORBIDDEN(HttpStatus.FORBIDDEN, "POLL403_1", "투표를 마감할 권한이 없습니다."),
    ALREADY_VOTED(HttpStatus.BAD_REQUEST, "POLL400_2", "이미 참여한 투표입니다."),
    MULTIPLE_CHOICE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "POLL400_3", "복수 선택이 허용되지 않은 투표입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
