package com.LionKing.Teamply.domain.meeting.dto.response;

import com.LionKing.Teamply.domain.meeting.entity.ActionItemStatus;
import java.time.LocalDateTime;
import java.util.List;

public class MeetingResDTO {

    public record MeetingCreateRes(
            Long meetingId,
            LocalDateTime createdAt
    ) {}

    public record MeetingUpdateRes(
            Long meetingId,
            LocalDateTime updatedAt
    ) {}

    public record MeetingGetRes(
            Long meetingId,
            String meetingTitle,
            LocalDateTime meetingDate,
            String rawContent,
            String discussion,
            String undecidedItems,
            List<AttendeeRes> attendees,
            List<ActionItemRes> actionItems
    ) {}

    public record AttendeeRes(
            Long userId,
            String name,
            String email
    ) {}

    public record ActionItemRes(
            Long actionItemId,
            String content,
            Long assigneeId,
            LocalDateTime deadline,
            ActionItemStatus status,
            Long linkedPostId
    ) {}
}
