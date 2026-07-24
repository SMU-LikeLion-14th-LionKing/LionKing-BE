package com.LionKing.Teamply.domain.project.dto.response;

import com.LionKing.Teamply.domain.project.entity.CalendarEvent;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record CalendarEventResponse(
        @JsonProperty("schedule_id")
        Long scheduleId,

        @JsonProperty("schedule_type")
        String scheduleType,

        String title,

        @JsonProperty("schedule_date")
        LocalDateTime scheduleDate,

        LocalDateTime deadline
) {
    public static CalendarEventResponse from(CalendarEvent event) {
        return new CalendarEventResponse(
                event.getId(),
                event.getEventType(),
                event.getTitle(),
                event.getEventDate(),
                event.getDeadLine()
        );
    }
}