package com.LionKing.Teamply.domain.project.service.command;

import com.LionKing.Teamply.domain.project.dto.request.*;
import com.LionKing.Teamply.domain.project.dto.response.*;
import com.LionKing.Teamply.domain.project.entity.*;
import com.LionKing.Teamply.domain.project.repository.*;
import com.LionKing.Teamply.domain.user.entity.User;
import com.LionKing.Teamply.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamCommandServiceImpl implements TeamCommandService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectLinkRepository projectLinkRepository;
    private final CalendarEventRepository calendarEventRepository;
    private final UserRepository userRepository;

    @Override
    public ProjectMemberInviteResponse inviteMember(Long projectId, ProjectMemberInviteRequest request) {
        Project project = getProjectOrThrow(projectId);

        if (!project.getTeamName().equals(request.teamName())) {
            throw new IllegalArgumentException("팀 이름이 일치하지 않습니다.");
        }

        User invitee = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 이메일입니다."));

        if (projectMemberRepository.existsByUserIdAndProjectId(invitee.getId(), projectId)) {
            throw new IllegalStateException("이미 참여 중인 사용자입니다.");
        }

        ProjectMember member = ProjectMember.builder()
                .user(invitee)
                .project(project)
                .role(ProjectRole.MEMBER)
                .build();
        projectMemberRepository.save(member);
        return ProjectMemberInviteResponse.from(member);
    }

    @Override
    public void updateMember(Long projectId, Long userId, ProjectMemberUpdateRequest request) {
        ProjectMember member = getMemberOrThrow(projectId, userId);

        if (request.permission() != null) {
            member.updateRole(parseRole(request.permission()));
        }
        if (request.roleDescription() != null) {
            member.updatePosition(request.roleDescription());
        }
    }

    @Override
    public void removeMember(Long projectId, Long userId) {
        ProjectMember member = getMemberOrThrow(projectId, userId);
        projectMemberRepository.delete(member);
    }

    @Override
    public ProjectLinkResponse addWorkspace(Long projectId, ProjectLinkCreateRequest request) {
        Project project = getProjectOrThrow(projectId);

        ProjectLink link = ProjectLink.builder()
                .project(project)
                .name(request.name())
                .url(request.url())
                .build();
        projectLinkRepository.save(link);
        return ProjectLinkResponse.from(link);
    }

    @Override
    public CalendarEventCreateResponse addCalendarEvent(Long projectId, CalendarEventCreateRequest request) {
        Project project = getProjectOrThrow(projectId);

        CalendarEvent event = CalendarEvent.builder()
                .project(project)
                .title(request.title())
                .eventType(request.eventType())
                .eventDate(request.eventDate())
                .build();
        calendarEventRepository.save(event);
        return CalendarEventCreateResponse.from(event);
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

    private ProjectRole parseRole(String value) {
        try {
            return ProjectRole.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 권한 값입니다: " + value);
        }
    }
}
