package com.LionKing.Teamply.domain.project.service.query;

import com.LionKing.Teamply.domain.project.converter.ProjectConverter;
import com.LionKing.Teamply.domain.project.dto.response.ProjectResDTO;
import com.LionKing.Teamply.domain.project.entity.Project;
import com.LionKing.Teamply.domain.project.entity.ProjectMember;
import com.LionKing.Teamply.domain.project.repository.ProjectMemberRepository;
import com.LionKing.Teamply.domain.project.repository.ProjectRepository;
import com.LionKing.Teamply.domain.project.exception.ProjectErrorCode;
import com.LionKing.Teamply.domain.project.exception.ProjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectQueryServiceImpl implements ProjectQueryService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public ProjectResDTO.ProjectGetRes getProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectException(ProjectErrorCode.PROJECT_NOT_FOUND));

        return ProjectConverter.toProjectGetRes(project);
    }

    @Override
    public List<ProjectResDTO.ProjectGetRes> getProjects(Long userId) {
        // ProjectMemberRepository를 통해 유저가 속한 멤버 목록을 가져온 후 프로젝트들로 변환
        List<Project> projectList = projectMemberRepository.findByUserId(userId).stream()
                .map(ProjectMember::getProject)
                .toList();
        
        return ProjectConverter.toProjectGetResList(projectList);
    }
}
