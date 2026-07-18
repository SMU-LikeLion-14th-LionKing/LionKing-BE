package com.LionKing.Teamply.domain.project.dto.response;

import com.LionKing.Teamply.domain.project.entity.CalendarEvent;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record CalendarEventCreateResponse(
        @JsonProperty("schedule_id")
        Long scheduleId,

        @JsonProperty("created_at")
        LocalDateTime createdAt
) {
    public static CalendarEventCreateResponse from(CalendarEvent event) {
        return new CalendarEventCreateResponse(
                event.getId(),
                event.getCreatedAt()
        );
    }
}