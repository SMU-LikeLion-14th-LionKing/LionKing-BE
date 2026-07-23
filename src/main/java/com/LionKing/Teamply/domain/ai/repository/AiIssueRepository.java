package com.LionKing.Teamply.domain.ai.repository;

import com.LionKing.Teamply.domain.ai.entity.AiIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiIssueRepository extends JpaRepository<AiIssue, Long> {
    List<AiIssue> findAllByProjectIdOrderByCreatedAtDesc(Long projectId);
}
