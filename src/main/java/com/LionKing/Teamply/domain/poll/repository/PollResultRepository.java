package com.LionKing.Teamply.domain.poll.repository;

import com.LionKing.Teamply.domain.poll.entity.PollResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PollResultRepository extends JpaRepository<PollResult, Long> {
    List<PollResult> findAllByPollId(Long pollId);
    void deleteByPollIdAndUserId(Long pollId, Long userId);
    Optional<PollResult> findByPollIdAndUserId(Long pollId, Long userId);
}
