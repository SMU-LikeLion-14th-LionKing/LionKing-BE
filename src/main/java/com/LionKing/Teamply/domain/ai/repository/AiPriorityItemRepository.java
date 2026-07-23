package com.LionKing.Teamply.domain.ai.repository;

import com.LionKing.Teamply.domain.ai.entity.AiPriorityItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiPriorityItemRepository extends JpaRepository<AiPriorityItem, Long> {
    List<AiPriorityItem> findAllByAiBriefingIdOrderByRankAsc(Long aiBriefingId);
}
