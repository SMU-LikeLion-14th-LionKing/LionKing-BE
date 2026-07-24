package com.LionKing.Teamply.domain.meeting.repository;

import com.LionKing.Teamply.domain.meeting.entity.MeetingMinute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetingMinuteRepository extends JpaRepository<MeetingMinute, Long> {
    
    // tset 브랜치에서 추가한 조회용 메서드
    Optional<MeetingMinute> findByPostId(Long postId);
    
    // develop 브랜치에 있던 삭제용 메서드
    void deleteByPost_Id(Long postId);
}