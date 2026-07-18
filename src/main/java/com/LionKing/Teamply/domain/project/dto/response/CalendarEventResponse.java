package com.LionKing.Teamply.domain.project.dto.response;

import com.LionKing.Teamply.domain.project.entity.CalendarEvent;

import java.time.LocalDateTime;

public record CalendarEventResponse(Long id, String title, String eventType, LocalDateTime eventDate) {
    public static CalendarEventResponse from(CalendarEvent event) {
        return new CalendarEventResponse(event.getId(), event.getTitle(), event.getEventType(), event.getEventDate());
    }
}