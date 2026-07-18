package com.LionKing.Teamply.domain.project.controller;

import com.LionKing.Teamply.domain.project.dto.request.*;
import com.LionKing.Teamply.domain.project.dto.response.*;
import com.LionKing.Teamply.domain.project.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}")
public class TeamController {

    private final TeamService teamService;

    // 1. 팀 선택
    @GetMapping
    public ResponseEntity<ProjectResponse> getProject(
            @PathVariable Long projectId,
            @AuthenticationUserId Long userId // 기존 인증 방식에 맞게 교체 필요
    ) {
        return ResponseEntity.ok(teamService.getProject(projectId, userId));
    }

    // 2. 팀원 추가
    @PostMapping("/members")
    public ResponseEntity<ProjectMemberResponse> inviteMember(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectMemberInviteRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teamService.inviteMember(projectId, request));
    }

    // 3. 팀 멤버 목록 조회
    @GetMapping("/members")
    public ResponseEntity<List<ProjectMemberResponse>> getMembers(@PathVariable Long projectId) {
        return ResponseEntity.ok(teamService.getMembers(projectId));
    }

    // 4. 팀원 수정
    @PatchMapping("/members")
    public ResponseEntity<ProjectMemberResponse> updateMember(
            @PathVariable Long projectId,
            @RequestBody ProjectMemberUpdateRequest request
    ) {
        return ResponseEntity.ok(teamService.updateMember(projectId, request));
    }

    // 5. 팀원 삭제
    @DeleteMapping("/members/{userId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long projectId,
            @PathVariable Long userId
    ) {
        teamService.removeMember(projectId, userId);
        return ResponseEntity.noContent().build();
    }

    // 6. 팀 워크스페이스 조회
    @GetMapping("/workspaces")
    public ResponseEntity<List<ProjectLinkResponse>> getWorkspaces(@PathVariable Long projectId) {
        return ResponseEntity.ok(teamService.getWorkspaces(projectId));
    }

    // 7. 팀 워크스페이스 추가
    @PostMapping("/workspaces")
    public ResponseEntity<ProjectLinkResponse> addWorkspace(
            @PathVariable Long projectId,
            @RequestBody ProjectLinkCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teamService.addWorkspace(projectId, request));
    }

    // 8. 팀 프로젝트 캘린더 조회
    @GetMapping("/calendar")
    public ResponseEntity<List<CalendarEventResponse>> getCalendarEvents(@PathVariable Long projectId) {
        return ResponseEntity.ok(teamService.getCalendarEvents(projectId));
    }

    // 9. 팀 캘린더 일정 추가
    @PostMapping("/calendar")
    public ResponseEntity<CalendarEventResponse> addCalendarEvent(
            @PathVariable Long projectId,
            @RequestBody CalendarEventCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teamService.addCalendarEvent(projectId, request));
    }
}