package com.LionKing.Teamply.domain.project.dto.request;

import java.time.LocalDateTime;

public record CalendarEventCreateRequest(String title, String eventType, LocalDateTime eventDate) {
}