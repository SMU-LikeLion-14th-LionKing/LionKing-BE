package com.LionKing.Teamply.domain.meeting.controller;

import com.LionKing.Teamply.domain.meeting.dto.request.MeetingReqDTO;
import com.LionKing.Teamply.domain.meeting.dto.response.MeetingResDTO;
import com.LionKing.Teamply.domain.meeting.service.command.MeetingCommandService;
import com.LionKing.Teamply.domain.meeting.service.query.MeetingQueryService;
import com.LionKing.Teamply.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Meeting Minute", description = "회의록 API")
@io.swagger.v3.oas.annotations.responses.ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "MEETING404: 존재하지 않는 회의록입니다.", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class)))
})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/meetings")
public class MeetingController {

    private final MeetingCommandService meetingCommandService;
    private final MeetingQueryService meetingQueryService;

    @Operation(summary = "회의록 생성", description = "새로운 회의록을 생성하고 참석자를 등록합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<MeetingResDTO.MeetingCreateRes>> createMeetingMinute(
            @PathVariable Long projectId,
            @AuthenticationPrincipal Long userId,
            @RequestBody MeetingReqDTO.MeetingCreateReq request
    ) {
        MeetingResDTO.MeetingCreateRes response = meetingCommandService.createMeetingMinute(projectId, userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "회의록 생성이 완료되었습니다.", response));
    }

    @Operation(summary = "회의록 상세 조회", description = "회의록 본문, 참석자, AI가 추출한 할 일 목록을 함께 조회합니다.")
    @GetMapping("/{meetingId}")
    public ResponseEntity<ApiResponse<MeetingResDTO.MeetingGetRes>> getMeetingMinute(
            @PathVariable Long projectId,
            @PathVariable Long meetingId
    ) {
        MeetingResDTO.MeetingGetRes response = meetingQueryService.getMeetingMinute(projectId, meetingId);
        return ResponseEntity.ok(ApiResponse.success(200, "회의록 상세 조회 성공", response));
    }

    @Operation(summary = "회의록 수정", description = "회의록의 내용 및 참석자를 수정합니다.")
    @PatchMapping("/{meetingId}")
    public ResponseEntity<ApiResponse<MeetingResDTO.MeetingUpdateRes>> updateMeetingMinute(
            @PathVariable Long projectId,
            @PathVariable Long meetingId,
            @RequestBody MeetingReqDTO.MeetingUpdateReq request
    ) {
        MeetingResDTO.MeetingUpdateRes response = meetingCommandService.updateMeetingMinute(projectId, meetingId, request);
        return ResponseEntity.ok(ApiResponse.success(200, "회의록 수정 완료", response));
    }

    @Operation(summary = "회의록 삭제", description = "회의록과 관련된 모든 정보를 삭제합니다.")
    @DeleteMapping("/{meetingId}")
    public ResponseEntity<ApiResponse<Void>> deleteMeetingMinute(
            @PathVariable Long projectId,
            @PathVariable Long meetingId
    ) {
        meetingCommandService.deleteMeetingMinute(projectId, meetingId);
        return ResponseEntity.ok(ApiResponse.success(200, "회의록 삭제 완료", null));
    }
}
