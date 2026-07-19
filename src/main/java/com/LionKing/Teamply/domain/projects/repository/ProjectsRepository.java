package com.LionKing.Teamply.domain.projects.repository;

import com.LionKing.Teamply.domain.projects.entity.Projects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectsRepository extends JpaRepository<Projects, Long> {

    List<Projects> findAllByUserEmail(String email);
}
