package com.LionKing.Teamply.domain.meeting.repository;

import com.LionKing.Teamply.domain.meeting.entity.ActionItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActionItemRepository extends JpaRepository<ActionItem, Long> {
    List<ActionItem> findAllByMeetingMinuteId(Long meetingMinuteId);
    void deleteAllByMeetingMinuteId(Long meetingMinuteId);
}
