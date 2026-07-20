package com.LionKing.Teamply.domain.project.service.command;

import com.LionKing.Teamply.domain.project.dto.request.ProjectReqDTO;
import com.LionKing.Teamply.domain.project.dto.response.ProjectResDTO;

public interface ProjectCommandService {
    ProjectResDTO.ProjectCreateRes createProject(ProjectReqDTO.ProjectCreateReq req, Long userId);
}
