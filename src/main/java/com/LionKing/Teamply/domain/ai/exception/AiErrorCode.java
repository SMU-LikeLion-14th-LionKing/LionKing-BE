package com.LionKing.Teamply.domain.ai.exception;

import com.LionKing.Teamply.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AiErrorCode implements BaseErrorCode {

    BRIEFING_NOT_FOUND(HttpStatus.NOT_FOUND, "AI404_1", "해당 주간 브리핑을 찾을 수 없습니다."),
    PRIORITY_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "AI404_2", "해당 우선순위 항목을 찾을 수 없습니다."),
    ISSUE_NOT_FOUND(HttpStatus.NOT_FOUND, "AI404_3", "해당 AI 발견 이슈를 찾을 수 없습니다."),
    GEMINI_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AI500_1", "Gemini API 호출 중 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
