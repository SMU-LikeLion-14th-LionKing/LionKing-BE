package com.LionKing.Teamply.domain.project.service;

import com.LionKing.Teamply.domain.project.dto.request.*;
import com.LionKing.Teamply.domain.project.dto.response.*;
import com.LionKing.Teamply.domain.project.entity.*;
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
public class TeamService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectLinkRepository projectLinkRepository;
    private final CalendarEventRepository calendarEventRepository;
    private final UserRepository userRepository;

    // 팀 선택 (프로젝트 단건 조회)
    public ProjectSelectResponse getProject(Long projectId, Long userId) {
        // 접근 권한 확인까지 한 번에 처리
        ProjectMember member = getMemberOrThrow(projectId, userId);
        return ProjectSelectResponse.from(member.getProject());
    }

    // 팀원 추가
    @Transactional
    public ProjectMemberInviteResponse inviteMember(Long projectId, ProjectMemberInviteRequest request) {
        Project project = getProjectOrThrow(projectId);

        if (!project.getName().equals(request.teamName())) {
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

    // 팀원 목록 조회
    public List<ProjectMemberListResponse> getMembers(Long projectId) {
        return projectMemberRepository.findByProjectId(projectId).stream()
                .map(ProjectMemberListResponse::from)
                .toList();
    }

    // 팀원 수정 (역할/포지션 변경)
    @Transactional
    public void updateMember(Long projectId, Long userId, ProjectMemberUpdateRequest request) {
        ProjectMember member = getMemberOrThrow(projectId, userId);

        if (request.permission() != null) {
            member.updateRole(parseRole(request.permission()));
        }
        if (request.roleDescription() != null) {
            member.updatePosition(request.roleDescription());
        }
    }

    // 팀원 삭제
    @Transactional
    public void removeMember(Long projectId, Long userId) {
        ProjectMember member = getMemberOrThrow(projectId, userId);
        projectMemberRepository.delete(member);
    }

    // 팀 워크스페이스 조회
    public List<ProjectLinkResponse> getWorkspaces(Long projectId) {
        return projectLinkRepository.findByProjectId(projectId).stream()
                .map(ProjectLinkResponse::from)
                .toList();
    }

    // 팀 워크스페이스 추가
    @Transactional
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

    // 팀 캘린더 조회
    public List<CalendarEventResponse> getCalendarEvents(Long projectId) {
        return calendarEventRepository.findByProjectId(projectId).stream()
                .map(CalendarEventResponse::from)
                .toList();
    }

    // 팀 캘린더 일정 추가
    @Transactional
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