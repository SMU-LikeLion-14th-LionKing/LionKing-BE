package com.LionKing.Teamply.domain.meeting.repository;

import com.LionKing.Teamply.domain.meeting.entity.MeetingMinute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingMinuteRepository extends JpaRepository<MeetingMinute, Long> {

    void deleteByPost_Id(Long postId);
}
