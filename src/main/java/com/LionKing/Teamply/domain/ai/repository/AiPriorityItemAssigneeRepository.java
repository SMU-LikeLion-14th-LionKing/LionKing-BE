package com.LionKing.Teamply.domain.ai.repository;

import com.LionKing.Teamply.domain.ai.entity.AiPriorityItemAssignee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiPriorityItemAssigneeRepository extends JpaRepository<AiPriorityItemAssignee, Long> {
    List<AiPriorityItemAssignee> findAllByPriorityItemId(Long priorityItemId);
}
