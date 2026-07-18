package com.LionKing.Teamply.domain.project.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record CalendarEventCreateRequest(
        @JsonProperty("event_type")
        String eventType,

        @JsonProperty("title")
        String title,

        @JsonProperty("event_date")
        LocalDateTime eventDate
) {
}