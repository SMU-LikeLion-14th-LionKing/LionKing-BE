package com.LionKing.Teamply.domain.project.service.query;

import com.LionKing.Teamply.domain.project.dto.response.ProjectResDTO;
import java.util.List;

public interface ProjectQueryService {
    ProjectResDTO.ProjectSummaryRes getProject(Long projectId);
    List<ProjectResDTO.ProjectListRes> getProjects(Long userId);
}
