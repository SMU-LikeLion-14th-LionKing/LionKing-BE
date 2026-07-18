package com.LionKing.Teamply.domain.project.repository;

import com.LionKing.Teamply.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}