package com.LionKing.Teamply.domain.project.service.query;

import com.LionKing.Teamply.domain.project.dto.response.*;
import com.LionKing.Teamply.domain.project.entity.Project;
import com.LionKing.Teamply.domain.project.entity.ProjectMember;
import com.LionKing.Teamply.domain.project.repository.*;
import com.LionKing.Teamply.domain.user.entity.User;
import com.LionKing.Teamply.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamQueryServiceImpl implements TeamQueryService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectLinkRepository projectLinkRepository;
    private final CalendarEventRepository calendarEventRepository;
    private final UserRepository userRepository;

    @Override
    public ProjectSelectResponse getProject(Long projectId, Long userId) {
        ProjectMember member = getMemberOrThrow(projectId, userId);
        return ProjectSelectResponse.from(member.getProject());
    }

    @Override
    public List<ProjectMemberListResponse> getMembers(Long projectId) {
        return projectMemberRepository.findByProjectId(projectId).stream()
                .map(ProjectMemberListResponse::from)
                .toList();
    }

    @Override
    public List<ProjectLinkResponse> getWorkspaces(Long projectId) {
        return projectLinkRepository.findByProjectId(projectId).stream()
                .map(ProjectLinkResponse::from)
                .toList();
    }

    @Override
    public List<CalendarEventResponse> getCalendarEvents(Long projectId) {
        return calendarEventRepository.findByProjectId(projectId).stream()
                .map(CalendarEventResponse::from)
                .toList();
    }

    private Project getProjectOrThrow(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }

    private ProjectMember getMemberOrThrow(Long projectId, Long userId) {
        Project project = getProjectOrThrow(projectId);
        User user = getUserOrThrow(userId);
        return projectMemberRepository.findByUserAndProject(user, project)
                .orElseThrow(() -> new IllegalArgumentException("팀원이 아닙니다."));
    }
}
