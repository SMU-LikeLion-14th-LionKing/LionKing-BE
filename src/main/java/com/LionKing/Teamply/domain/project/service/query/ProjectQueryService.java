package com.LionKing.Teamply.domain.project.service.query;

import com.LionKing.Teamply.domain.project.dto.response.ProjectResDTO;
import java.util.List;

public interface ProjectQueryService {
    ProjectResDTO.ProjectGetRes getProject(Long projectId);
    List<ProjectResDTO.ProjectGetRes> getProjects(Long userId);
}
