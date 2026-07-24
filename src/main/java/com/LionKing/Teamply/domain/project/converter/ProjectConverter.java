package com.LionKing.Teamply.domain.project.converter;

import com.LionKing.Teamply.domain.project.dto.request.ProjectReqDTO;
import com.LionKing.Teamply.domain.project.dto.response.ProjectResDTO;
import com.LionKing.Teamply.domain.project.entity.Project;

import java.util.List;

public class ProjectConverter {

    public static Project toProject(ProjectReqDTO.ProjectCreateReq req) {
        return Project.builder()
                .name(req.name())
                .projectType(req.projectType())
                .title(req.title())
                .deadline(req.deadline())
                .build(); // progressRate, aiProgressRate는 엔티티 빌더에서 0f로 초기화됨
    }

    public static ProjectResDTO.ProjectCreateRes toProjectCreateRes(Project project) {
        return new ProjectResDTO.ProjectCreateRes(
                project.getId(),
                project.getCreatedAt()
        );
    }

    public static ProjectResDTO.ProjectGetRes toProjectGetRes(Project project) {
        return new ProjectResDTO.ProjectGetRes(
                project.getId(),
                project.getName(),
                project.getTitle(),
                project.getProjectType(),
                project.getDeadline(),
                project.getProgressRate(),
                project.getAiProgressRate(),
                project.getTotalTaskCount()
        );
    }

    public static List<ProjectResDTO.ProjectGetRes> toProjectGetResList(List<Project> projectList) {
        return projectList.stream()
                .map(ProjectConverter::toProjectGetRes)
                .toList();
    }
}
