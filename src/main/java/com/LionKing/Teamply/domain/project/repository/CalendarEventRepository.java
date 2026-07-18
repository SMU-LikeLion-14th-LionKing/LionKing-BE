package com.LionKing.Teamply.domain.project.repository;

import com.LionKing.Teamply.domain.project.entity.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {
    List<CalendarEvent> findByProjectId(Long projectId);
}