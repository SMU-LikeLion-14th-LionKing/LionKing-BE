package com.LionKing.Teamply.domain.project.repository;

import com.LionKing.Teamply.domain.project.entity.Project;
import com.LionKing.Teamply.domain.project.entity.ProjectMember;
import com.LionKing.Teamply.domain.project.entity.ProjectMemberId;
import com.LionKing.Teamply.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {

    // 내가 참여 중인 프로젝트 목록 (팀 선택 화면, 사이드메뉴 참여 팀 목록)
    List<ProjectMember> findByUserId(Long userId);

    // 특정 프로젝트의 팀원 목록 (팀원 설정 화면)
    List<ProjectMember> findByProjectId(Long projectId);

    long countByProjectId(Long projectId);

    // 특정 프로젝트에 특정 유저가 이미 속해있는지 확인
    Optional<ProjectMember> findByUserAndProject(User user, Project project);

    boolean existsByUserIdAndProjectId(Long userId, Long projectId);
}