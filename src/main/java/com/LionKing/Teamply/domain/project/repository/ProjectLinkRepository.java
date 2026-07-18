package com.LionKing.Teamply.domain.project.repository;

import com.LionKing.Teamply.domain.project.entity.ProjectLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectLinkRepository extends JpaRepository<ProjectLink, Long> {
    List<ProjectLink> findByProjectId(Long projectId);
}