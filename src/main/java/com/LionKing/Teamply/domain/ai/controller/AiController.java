package com.LionKing.Teamply.domain.ai.controller;

import com.LionKing.Teamply.domain.ai.dto.request.AiReqDTO.*;
import com.LionKing.Teamply.domain.ai.dto.response.AiResDTO.*;
import com.LionKing.Teamply.domain.ai.service.command.AiCommandService;
import com.LionKing.Teamply.domain.ai.service.query.AiQueryService;
import com.LionKing.Teamply.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AI 협업 도우미", description = "AI 텍스트 분석(협업 매니저, 일정/회의록 추출) 및 AI 주간 브리핑, 우선순위, 이슈 조회 API")
@io.swagger.v3.oas.annotations.responses.ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "생성 성공", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "AI404_1: 해당 주간 브리핑을 찾을 수 없습니다.", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "AI404_2: 해당 우선순위 항목을 찾을 수 없습니다.", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "AI404_3: 해당 AI 발견 이슈를 찾을 수 없습니다.", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "AI500_1: Gemini API 호출 중 오류가 발생했습니다.", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class)))
})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AiController {

    private final AiCommandService aiCommandService;
    private final AiQueryService aiQueryService;

    // --- 기능형 AI API (텍스트 분석) ---

    @Operation(summary = "AI 협업 매니저 텍스트 분석", description = "텍스트(댓글, 게시글 등)를 분석하여 모호한 부분을 지적하고 수정 예시를 제안합니다.")
    @PostMapping("/ai/collaborate-manager")
    public ResponseEntity<ApiResponse<CollaborateManagerRes>> analyzeCollaboration(
            @Valid @RequestBody CollaborateManagerReq request
    ) {
        CollaborateManagerRes response = aiCommandService.analyzeCollaboration(request);
        return ResponseEntity.ok(ApiResponse.success(200, "AI 협업 매니저 분석 성공", response));
    }

    @Operation(summary = "AI 회의록 추출", description = "긴 회의록이나 채팅 내역을 요약하여 회의 목적, 논의, 결과를 추출합니다.")
    @PostMapping("/ai/meeting-minutes")
    public ResponseEntity<ApiResponse<MeetingMinutesRes>> extractMeetingMinutes(
            @Valid @RequestBody MeetingMinutesReq request
    ) {
        MeetingMinutesRes response = aiCommandService.extractMeetingMinutes(request);
        return ResponseEntity.ok(ApiResponse.success(200, "AI 회의록 추출 성공", response));
    }

    @Operation(summary = "AI 팀 일정 캘린더 생성", description = "회의록이나 일정 관련 텍스트를 분석하여 캘린더에 등록할 일정 목록을 JSON 형식으로 추출합니다.")
    @PostMapping("/ai/schedule-parse")
    public ResponseEntity<ApiResponse<ScheduleParseRes>> parseSchedules(
            @Valid @RequestBody ScheduleParseReq request
    ) {
        ScheduleParseRes response = aiCommandService.parseSchedules(request);
        return ResponseEntity.ok(ApiResponse.success(200, "AI 일정 추출 성공", response));
    }

    // --- 조회형 AI API (DB 기반) ---

    @Operation(summary = "AI 추정 진행률 조회", description = "프로젝트의 실제 진행률과 AI 추정 진행률을 비교 조회합니다.")
    @GetMapping("/projects/{projectId}/ai-progress")
    public ResponseEntity<ApiResponse<ProgressRateRes>> getAiProgressRate(
            @PathVariable Long projectId
    ) {
        ProgressRateRes response = aiQueryService.getAiProgressRate(projectId);
        return ResponseEntity.ok(ApiResponse.success(200, "AI 추정 진행률 조회 성공", response));
    }

    @Operation(summary = "AI 이번 주 우선순위 목록 조회", description = "가장 최근의 주간 브리핑 요약과 그에 속한 TOP 우선순위 작업들을 조회합니다.")
    @GetMapping("/projects/{projectId}/ai-priorities")
    public ResponseEntity<ApiResponse<PriorityBriefingRes>> getWeeklyPriorities(
            @PathVariable Long projectId
    ) {
        PriorityBriefingRes response = aiQueryService.getWeeklyPriorities(projectId);
        return ResponseEntity.ok(ApiResponse.success(200, "AI 주간 우선순위 조회 성공", response));
    }

    @Operation(summary = "AI 우선순위 상세 조회", description = "특정 우선순위 항목의 세부 내용과 담당자 목록을 조회합니다.")
    @GetMapping("/projects/{projectId}/ai-priorities/{priorityId}")
    public ResponseEntity<ApiResponse<PriorityItemRes>> getPriorityDetail(
            @PathVariable Long projectId,
            @PathVariable Long priorityId
    ) {
        PriorityItemRes response = aiQueryService.getPriorityDetail(projectId, priorityId);
        return ResponseEntity.ok(ApiResponse.success(200, "AI 우선순위 상세 조회 성공", response));
    }

    @Operation(summary = "AI 발견 이슈 목록 조회", description = "소통 오류, 일정 지연 등 AI가 감지한 해당 프로젝트의 이슈 목록을 조회합니다.")
    @GetMapping("/projects/{projectId}/ai-issues")
    public ResponseEntity<ApiResponse<IssueListRes>> getAiIssues(
            @PathVariable Long projectId
    ) {
        IssueListRes response = aiQueryService.getAiIssues(projectId);
        return ResponseEntity.ok(ApiResponse.success(200, "AI 발견 이슈 목록 조회 성공", response));
    }

    @Operation(summary = "AI 발견 이슈 상세 조회", description = "특정 AI 발견 이슈의 원인 및 개선 제안 등 상세 내용을 조회합니다.")
    @GetMapping("/projects/{projectId}/ai-issues/{issueId}")
    public ResponseEntity<ApiResponse<IssueRes>> getAiIssueDetail(
            @PathVariable Long projectId,
            @PathVariable Long issueId
    ) {
        IssueRes response = aiQueryService.getAiIssueDetail(projectId, issueId);
        return ResponseEntity.ok(ApiResponse.success(200, "AI 발견 이슈 상세 조회 성공", response));
    }
}
