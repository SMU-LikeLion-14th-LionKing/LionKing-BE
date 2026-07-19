package com.LionKing.Teamply.domain.projects.service.query;

import com.LionKing.Teamply.domain.projects.converter.ProjectConverter;
import com.LionKing.Teamply.domain.projects.dto.response.ProjectsResDTO;
import com.LionKing.Teamply.domain.projects.entity.Projects;
import com.LionKing.Teamply.domain.projects.exception.ProjectErrorCode;
import com.LionKing.Teamply.domain.projects.exception.ProjectException;
import com.LionKing.Teamply.domain.projects.repository.ProjectsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectQueryServiceImpl implements ProjectQueryService{

    private final ProjectsRepository projectsRepository;

    @Override
    public ProjectsResDTO.ProjectGetRes getProject(Long projectId){

        Projects project = projectsRepository.findById(projectId)
                .orElseThrow(() -> new ProjectException(ProjectErrorCode.PROJECT_NOT_FOUND));

        return ProjectConverter.toProjectGetRes(project);
    }

    @Override
    public List<ProjectsResDTO.ProjectGetRes> getProjects(String email) { // 리턴 타입을 List로 변경
        // 1. 레포지토리 메서드명 수정 (_Email)
        List<Projects> projectList = projectsRepository.findAllByUserEmail(email);
        // 2. 컨버터에서 리스트용 변환 메서드 호출
        return ProjectConverter.toProjectGetResList(projectList);
    }
}
