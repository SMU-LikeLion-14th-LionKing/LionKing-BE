package com.LionKing.Teamply.domain.projects.converter;

import com.LionKing.Teamply.domain.projects.dto.request.ProjectsReqDTO;
import com.LionKing.Teamply.domain.projects.dto.response.ProjectsResDTO;
import com.LionKing.Teamply.domain.projects.entity.Projects;

import java.util.List;

public class ProjectConverter {

    public static Projects toProject(ProjectsReqDTO.ProjectCreateReq req) {
        return Projects.builder()
                .name(req.name())
                .projectType(req.projectType())
                .title(req.title())
                .deadline(req.deadline())
                .progressRate(0.0f)
                .aiProgressRate(0.0f)
                .build();
    }

    public static ProjectsResDTO.ProjectCreateRes toProjectCreateRes(Projects project) {
        return new ProjectsResDTO.ProjectCreateRes(
                project.getId(),
                project.getCreatedAt()
        );
    }

    public static ProjectsResDTO.ProjectGetRes toProjectGetRes(Projects project) {
        return new ProjectsResDTO.ProjectGetRes(
                project.getTitle(),
                project.getDeadline()
        );
    }

    public static List<ProjectsResDTO.ProjectGetRes> toProjectGetResList(List<Projects> projectList) {
        return projectList.stream()
                .map(ProjectConverter::toProjectGetRes)
                .toList();
    }
}
