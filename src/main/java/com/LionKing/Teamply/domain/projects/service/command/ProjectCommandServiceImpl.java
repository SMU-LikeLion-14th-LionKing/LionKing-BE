package com.LionKing.Teamply.domain.projects.service.command;

import com.LionKing.Teamply.domain.projects.converter.ProjectConverter;
import com.LionKing.Teamply.domain.projects.dto.request.ProjectsReqDTO;
import com.LionKing.Teamply.domain.projects.dto.response.ProjectsResDTO;
import com.LionKing.Teamply.domain.projects.entity.Projects;
import com.LionKing.Teamply.domain.projects.repository.ProjectsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectCommandServiceImpl implements ProjectCommandService{

    private final ProjectsRepository projectsRepository;

    @Override
    public ProjectsResDTO.ProjectCreateRes createProject(ProjectsReqDTO.ProjectCreateReq req){

        Projects projects = ProjectConverter.toProject(req);

        Projects savedProject = projectsRepository.save(projects);

        return ProjectConverter.toProjectCreateRes(savedProject);
    }
}
