package com.LionKing.Teamply.domain.meeting.repository;

import com.LionKing.Teamply.domain.meeting.entity.MeetingMinute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetingMinuteRepository extends JpaRepository<MeetingMinute, Long> {
    Optional<MeetingMinute> findByPostId(Long postId);
}
