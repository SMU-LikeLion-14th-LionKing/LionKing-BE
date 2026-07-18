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
    public ProjectResponse getProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));
        ProjectMember member = projectMemberRepository.findByUserAndProject(
                        userRepository.getReferenceById(userId), project)
                .orElseThrow(() -> new IllegalStateException("접근 권한이 없습니다."));
        return ProjectResponse.of(project, member.getRole());
    }

    // 팀원 추가
    @Transactional
    public ProjectMemberResponse inviteMember(Long projectId, ProjectMemberInviteRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));
        User invitee = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 이메일입니다."));

        if (projectMemberRepository.existsByUserIdAndProjectId(invitee.getId(), projectId)) {
            throw new IllegalStateException("이미 참여 중인 사용자입니다.");
        }

        ProjectMember member = ProjectMember.builder()
                .user(invitee)
                .project(project)
                .role(ProjectMember.ROLE_MEMBER)
                .build();
        projectMemberRepository.save(member);
        return ProjectMemberResponse.from(member);
    }

    // 팀원 목록 조회
    public List<ProjectMemberResponse> getMembers(Long projectId) {
        return projectMemberRepository.findByProjectId(projectId).stream()
                .map(ProjectMemberResponse::from)
                .toList();
    }

    // 팀원 수정 (역할/포지션 변경)
    @Transactional
    public ProjectMemberResponse updateMember(Long projectId, ProjectMemberUpdateRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        ProjectMember member = projectMemberRepository.findByUserAndProject(user, project)
                .orElseThrow(() -> new IllegalArgumentException("팀원이 아닙니다."));

        if (request.role() != null) member.updateRole(request.role());
        if (request.position() != null) member.updatePosition(request.position());

        return ProjectMemberResponse.from(member);
    }

    // 팀원 삭제
    @Transactional
    public void removeMember(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        ProjectMember member = projectMemberRepository.findByUserAndProject(user, project)
                .orElseThrow(() -> new IllegalArgumentException("팀원이 아닙니다."));

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
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));

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
    public CalendarEventResponse addCalendarEvent(Long projectId, CalendarEventCreateRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));

        CalendarEvent event = CalendarEvent.builder()
                .project(project)
                .title(request.title())
                .eventType(request.eventType())
                .eventDate(request.eventDate())
                .build();
        calendarEventRepository.save(event);
        return CalendarEventResponse.from(event);
    }
}