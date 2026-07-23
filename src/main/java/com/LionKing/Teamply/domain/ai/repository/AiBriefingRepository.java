package com.LionKing.Teamply.domain.ai.repository;

import com.LionKing.Teamply.domain.ai.entity.AiBriefing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AiBriefingRepository extends JpaRepository<AiBriefing, Long> {
    Optional<AiBriefing> findTopByProjectIdOrderByUpdatedAtDesc(Long projectId);
}
