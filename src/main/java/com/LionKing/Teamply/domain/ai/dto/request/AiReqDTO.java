package com.LionKing.Teamply.domain.ai.dto.request;

import jakarta.validation.constraints.NotBlank;

public class AiReqDTO {

    public record CollaborateManagerReq(
            @NotBlank(message = "분석할 텍스트를 입력해주세요.")
            String content
    ) {}

    public record MeetingMinutesReq(
            @jakarta.validation.constraints.NotNull(message = "프로젝트 ID를 입력해주세요.")
            Long projectId,
            @NotBlank(message = "회의록 텍스트를 입력해주세요.")
            String meetingNotes
    ) {}

    public record ScheduleParseReq(
            @NotBlank(message = "분석할 일정 텍스트를 입력해주세요.")
            String scheduleNotes
    ) {}
}
