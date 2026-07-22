package com.LionKing.Teamply.domain.poll.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public record PollReqDTO() {

    public record CreatePollReq(
            @NotBlank(message = "투표 질문은 필수입니다.")
            String question,

            @NotNull(message = "복수 선택 여부는 필수입니다.")
            Boolean isMultipleChoice,

            @NotNull(message = "마감 시간은 필수입니다.")
            @Future(message = "마감 시간은 현재 시간 이후여야 합니다.")
            LocalDateTime deadline,

            @NotEmpty(message = "선택지는 최소 1개 이상이어야 합니다.")
            List<OptionReq> options
    ) {}

    public record OptionReq(
            @NotBlank(message = "선택지 내용은 필수입니다.")
            String content,
            String imageUrl
    ) {}

    public record CastVoteReq(
            @NotEmpty(message = "최소 1개의 선택지에 투표해야 합니다.")
            List<Long> optionIds
    ) {}

    public record UpdateDeadlineReq(
            @NotNull(message = "새로운 마감 시간은 필수입니다.")
            @Future(message = "마감 시간은 현재 시간 이후여야 합니다.")
            LocalDateTime newDeadline
    ) {}
}
