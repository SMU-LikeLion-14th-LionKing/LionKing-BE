package com.LionKing.Teamply.domain.project.controller;

import com.LionKing.Teamply.domain.project.dto.request.*;
import com.LionKing.Teamply.domain.project.dto.response.*;
import com.LionKing.Teamply.domain.project.service.TeamService;
import com.LionKing.Teamply.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Team", description = "팀 선택 / 팀원 관리 / 워크스페이스 / 팀 캘린더 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}")
public class TeamController {

    private final TeamService teamService;

    // 1. 팀 선택
    @Operation(summary = "팀 선택", description = "참여 중인 프로젝트 팀 중 하나를 선택하여 상세 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<ProjectSelectResponse>> getProject(
            @PathVariable Long projectId,
            @AuthenticationPrincipal Long userId
    ) {
        ProjectSelectResponse response = teamService.getProject(projectId, userId);
        return ResponseEntity.ok(ApiResponse.success(200, "프로젝트 정보 조회 성공", response));
    }

    // 2. 팀원 추가
    @Operation(summary = "팀원 추가", description = "이메일로 팀원을 프로젝트에 초대합니다.")
    @PostMapping("/members")
    public ResponseEntity<ApiResponse<ProjectMemberInviteResponse>> inviteMember(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectMemberInviteRequest request
    ) {
        ProjectMemberInviteResponse response = teamService.inviteMember(projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "팀원이 성공적으로 추가되었습니다.", response));
    }

    // 3. 팀 멤버 목록 조회
    @Operation(summary = "팀 멤버 목록 조회", description = "프로젝트에 참여 중인 팀원 목록을 조회합니다.")
    @GetMapping("/members")
    public ResponseEntity<ApiResponse<List<ProjectMemberListResponse>>> getMembers(@PathVariable Long projectId) {
        List<ProjectMemberListResponse> response = teamService.getMembers(projectId);
        return ResponseEntity.ok(ApiResponse.success(200, "팀 멤버 목록 조회 성공", response));
    }

    // 4. 팀원 수정
    @Operation(summary = "팀원 수정", description = "팀원의 역할(팀장/팀원) 또는 담당 포지션을 수정합니다.")
    @PatchMapping("/members/{userId}")
    public ResponseEntity<ApiResponse<Void>> updateMember(
            @PathVariable Long projectId,
            @PathVariable Long userId,
            @RequestBody ProjectMemberUpdateRequest request
    ) {
        teamService.updateMember(projectId, userId, request);
        return ResponseEntity.ok(ApiResponse.success(200, "팀원 역할이 수정되었습니다.", null));
    }

    // 5. 팀원 삭제
    @Operation(summary = "팀원 삭제", description = "프로젝트에서 특정 팀원을 제외합니다.")
    @DeleteMapping("/members/{userId}")
    public ResponseEntity<ApiResponse<Void>> removeMember(
            @PathVariable Long projectId,
            @PathVariable Long userId
    ) {
        teamService.removeMember(projectId, userId);
        return ResponseEntity.ok(ApiResponse.success(200, "해당 팀원이 프로젝트에서 제외되었습니다.", null));
    }

    // 6. 팀 워크스페이스 조회
    @Operation(summary = "팀 워크스페이스 조회", description = "팀에 등록된 협업 툴(피그마, 노션 등) 링크 목록을 조회합니다.")
    @GetMapping("/workspaces")
    public ResponseEntity<ApiResponse<List<ProjectLinkResponse>>> getWorkspaces(@PathVariable Long projectId) {
        List<ProjectLinkResponse> response = teamService.getWorkspaces(projectId);
        return ResponseEntity.ok(ApiResponse.success(200, "워크스페이스 목록 조회 성공", response));
    }

    // 7. 팀 워크스페이스 추가
    @Operation(summary = "팀 워크스페이스 추가", description = "팀에 새로운 협업 툴 워크스페이스 링크를 등록합니다.")
    @PostMapping("/workspaces")
    public ResponseEntity<ApiResponse<ProjectLinkResponse>> addWorkspace(
            @PathVariable Long projectId,
            @RequestBody ProjectLinkCreateRequest request
    ) {
        ProjectLinkResponse response = teamService.addWorkspace(projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "워크스페이스가 성공적으로 등록되었습니다.", response));
    }

    // 8. 팀 프로젝트 캘린더 조회
    @Operation(summary = "팀 프로젝트 캘린더 조회", description = "팀의 회의/마감/작업 일정을 캘린더 형태로 조회합니다.")
    @GetMapping("/calendar")
    public ResponseEntity<ApiResponse<List<CalendarEventResponse>>> getCalendarEvents(@PathVariable Long projectId) {
        List<CalendarEventResponse> response = teamService.getCalendarEvents(projectId);
        return ResponseEntity.ok(ApiResponse.success(200, "월별 일정 조회 성공", response));
    }

    // 9. 팀 캘린더 일정 추가
    @Operation(summary = "팀 캘린더 일정 추가", description = "팀원이 캘린더에 직접 일정을 추가합니다.")
    @PostMapping("/calendar")
    public ResponseEntity<ApiResponse<CalendarEventCreateResponse>> addCalendarEvent(
            @PathVariable Long projectId,
            @RequestBody CalendarEventCreateRequest request
    ) {
        CalendarEventCreateResponse response = teamService.addCalendarEvent(projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "일정이 성공적으로 추가되었습니다.", response));
    }
}