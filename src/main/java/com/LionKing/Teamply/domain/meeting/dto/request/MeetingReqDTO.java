package com.LionKing.Teamply.domain.meeting.dto.request;

import java.time.LocalDateTime;
import java.util.List;

public class MeetingReqDTO {

    public record MeetingCreateReq(
            String meetingTitle,
            LocalDateTime meetingDate,
            String rawContent,
            List<Long> attendeeUserIds
    ) {}

    public record MeetingUpdateReq(
            String meetingTitle,
            LocalDateTime meetingDate,
            String rawContent,
            List<Long> attendeeUserIds
    ) {}
}
