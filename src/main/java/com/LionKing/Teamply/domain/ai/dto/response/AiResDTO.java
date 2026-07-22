package com.LionKing.Teamply.domain.ai.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class AiResDTO {

    public record ProgressRateRes(
            double currentProgressRate,
            double aiEstimatedProgressRate
    ) {}

    public record CollaborateManagerRes(
            List<String> feedbackPoints,
            List<String> suggestions,
            String suggestedText
    ) {}

    public record MeetingMinutesRes(
            String meetingGoal,
            List<String> keyDiscussions,
            List<String> decisions,
            int totalTaskCount
    ) {}

    public record ScheduleParseItemRes(
            String title,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String memo
    ) {}

    public record ScheduleParseRes(
            List<ScheduleParseItemRes> schedules
    ) {}

    public record PriorityAssigneeRes(
            Long userId,
            String name,
            String profileImageUrl
    ) {}

    public record PriorityItemRes(
            Long priorityId,
            Integer rank,
            String content,
            LocalDateTime deadline,
            List<PriorityAssigneeRes> assignees
    ) {}

    public record PriorityBriefingRes(
            Long briefingId,
            String summary,
            List<PriorityItemRes> priorities
    ) {}

    public record IssueRes(
            Long issueId,
            String category,
            String riskLevel,
            String cause,
            String suggestion,
            LocalDateTime createdAt
    ) {}

    public record IssueListRes(
            List<IssueRes> issues
    ) {}
}
