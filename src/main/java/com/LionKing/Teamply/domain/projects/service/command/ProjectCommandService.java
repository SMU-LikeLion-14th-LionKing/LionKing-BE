package com.LionKing.Teamply.domain.projects.service.command;

import com.LionKing.Teamply.domain.projects.dto.request.ProjectsReqDTO;
import com.LionKing.Teamply.domain.projects.dto.response.ProjectsResDTO;

public interface ProjectCommandService {

    ProjectsResDTO.ProjectCreateRes createProject(ProjectsReqDTO.ProjectCreateReq req);
}
