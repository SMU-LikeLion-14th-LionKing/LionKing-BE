package com.LionKing.Teamply.domain.project.service.command;

import com.LionKing.Teamply.domain.project.converter.ProjectConverter;
import com.LionKing.Teamply.domain.project.dto.request.ProjectReqDTO;
import com.LionKing.Teamply.domain.project.dto.response.ProjectResDTO;
import com.LionKing.Teamply.domain.project.entity.Project;
import com.LionKing.Teamply.domain.project.entity.ProjectMember;
import com.LionKing.Teamply.domain.project.entity.ProjectRole;
import com.LionKing.Teamply.domain.project.repository.ProjectMemberRepository;
import com.LionKing.Teamply.domain.project.repository.ProjectRepository;
import com.LionKing.Teamply.domain.user.entity.User;
import com.LionKing.Teamply.domain.user.repository.UserRepository;
import com.LionKing.Teamply.global.apiPayload.code.GeneralErrorCode;
import com.LionKing.Teamply.global.apiPayload.exception.handler.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectCommandServiceImpl implements ProjectCommandService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;

    @Override
    public ProjectResDTO.ProjectCreateRes createProject(ProjectReqDTO.ProjectCreateReq req, Long userId) {
        // 1. 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));

        // 2. 프로젝트 엔티티 변환 및 저장
        Project project = ProjectConverter.toProject(req);
        Project savedProject = projectRepository.save(project);

        // 3. 작성자를 ProjectMember(LEADER)로 등록
        ProjectMember leaderMember = ProjectMember.builder()
                .user(user)
                .project(savedProject)
                .role(ProjectRole.LEADER)
                .build();
        projectMemberRepository.save(leaderMember);

        // 4. 응답 DTO 변환
        return ProjectConverter.toProjectCreateRes(savedProject);
    }
}
