package com.LionKing.Teamply.domain.poll.repository;

import com.LionKing.Teamply.domain.poll.entity.PollOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PollOptionRepository extends JpaRepository<PollOption, Long> {
    List<PollOption> findAllByPollId(Long pollId);
}
