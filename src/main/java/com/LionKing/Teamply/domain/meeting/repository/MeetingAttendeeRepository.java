package com.LionKing.Teamply.domain.meeting.repository;

import com.LionKing.Teamply.domain.meeting.entity.MeetingAttendee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingAttendeeRepository extends JpaRepository<MeetingAttendee, Long> {
    List<MeetingAttendee> findAllByMeetingMinuteId(Long meetingMinuteId);
    void deleteAllByMeetingMinuteId(Long meetingMinuteId);
}
