package com.LionKing.Teamply.domain.projects.service.query;

import com.LionKing.Teamply.domain.projects.dto.response.ProjectsResDTO;

import java.util.List;

public interface ProjectQueryService {

    ProjectsResDTO.ProjectGetRes getProject(Long projectId);

    List<ProjectsResDTO.ProjectGetRes> getProjects(String email);
}
