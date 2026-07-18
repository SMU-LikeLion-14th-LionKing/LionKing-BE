package com.LionKing.Teamply.domain.project.dto.response;

import com.LionKing.Teamply.domain.project.entity.Project;

import java.time.LocalDateTime;

public record ProjectResponse(
        Long id,
        String name,
        String projectType,
        String title,
        LocalDateTime deadline,
        Float progressRate,
        Float aiProgressRate,
        String myRole // 현재 로그인한 유저의 이 프로젝트 내 역할
) {
    public static ProjectResponse of(Project project, String myRole) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getProjectType(),
                project.getTitle(),
                project.getDeadline(),
                project.getProgressRate(),
                project.getAiProgressRate(),
                myRole
        );
    }
}