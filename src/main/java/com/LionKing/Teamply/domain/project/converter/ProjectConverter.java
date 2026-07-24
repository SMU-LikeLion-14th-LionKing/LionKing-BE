package com.LionKing.Teamply.domain.project.converter;

import com.LionKing.Teamply.domain.project.dto.request.ProjectReqDTO;
import com.LionKing.Teamply.domain.project.dto.response.ProjectResDTO;
import com.LionKing.Teamply.domain.project.entity.Project;

import java.util.List;

public class ProjectConverter {

    public static Project toProject(ProjectReqDTO.ProjectCreateReq req) {
        return Project.builder()
                .teamName(req.name())
                .projectType(req.projectType())
                .title(req.title())
                .deadline(req.deadline())
                .build();
    }

    public static ProjectResDTO.ProjectCreateRes toProjectCreateRes(Project project) {
        return new ProjectResDTO.ProjectCreateRes(
                project.getId(),
                project.getCreatedAt()
        );
    }

    public static ProjectResDTO.ProjectListRes toProjectListRes(Project project) {
        return new ProjectResDTO.ProjectListRes(
                project.getId(),
                project.getTeamName()
        );
    }

    public static ProjectResDTO.ProjectSummaryRes toProjectSummaryRes(Project project) {
        return new ProjectResDTO.ProjectSummaryRes(
                project.getId(),
                project.getTeamName(),
                project.getTitle(),
                project.getProjectType(),
                project.getDeadline(),
                project.getProgressRate(),
                project.getAiProgressRate(),
                project.getTotalTaskCount()
        );
    }

    public static List<ProjectResDTO.ProjectListRes> toProjectListResList(List<Project> projectList) {
        return projectList.stream()
                .map(ProjectConverter::toProjectListRes)
                .toList();
    }
}
