package com.LionKing.Teamply.domain.project.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "calendar_events")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CalendarEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "event_type", length = 50)
    private String eventType; // 회의/마감/작업

    @Column(length = 255)
    private String title;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Builder
    public CalendarEvent(Project project, String eventType, String title, LocalDateTime eventDate) {
        this.project = project;
        this.eventType = eventType;
        this.title = title;
        this.eventDate = eventDate;
    }
}