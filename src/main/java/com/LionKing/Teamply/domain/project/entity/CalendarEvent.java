package com.LionKing.Teamply.domain.project.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "calendar_events")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class CalendarEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "event_type", length = 50)
    private String eventType;

    @Column(length = 255)
    private String title;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public CalendarEvent(Project project, String eventType, String title, LocalDateTime eventDate) {
        this.project = project;
        this.eventType = eventType;
        this.title = title;
        this.eventDate = eventDate;
    }
}