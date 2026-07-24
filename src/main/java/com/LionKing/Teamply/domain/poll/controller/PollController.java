package com.LionKing.Teamply.domain.poll.controller;

import com.LionKing.Teamply.domain.poll.dto.request.PollReqDTO.CastVoteReq;
import com.LionKing.Teamply.domain.poll.dto.request.PollReqDTO.CreatePollReq;
import com.LionKing.Teamply.domain.poll.dto.request.PollReqDTO.UpdateDeadlineReq;
import com.LionKing.Teamply.domain.poll.dto.response.PollResDTO.PollDetailRes;
import com.LionKing.Teamply.domain.poll.dto.response.PollResDTO.PollResultRes;
import com.LionKing.Teamply.domain.poll.service.command.PollCommandService;
import com.LionKing.Teamply.domain.poll.service.query.PollQueryService;
import com.LionKing.Teamply.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Poll (투표)", description = "투표 생성, 상세/결과 조회, 투표하기 및 상태 변경 API")
@io.swagger.v3.oas.annotations.responses.ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "POLL404_1: 투표를 찾을 수 없습니다.", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "POLL404_2: 투표 선택지를 찾을 수 없습니다.", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "POLL400_1: 이미 마감된 투표입니다.", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "POLL403_1: 투표를 마감할 권한이 없습니다.", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "POLL400_2: 이미 참여한 투표입니다.", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "POLL400_3: 복수 선택이 허용되지 않은 투표입니다.", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class)))
})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PollController {

    private final PollCommandService pollCommandService;
    private final PollQueryService pollQueryService;

    @Operation(summary = "투표 생성", description = "특정 게시글(Post) 내에 새로운 투표와 선택지들을 함께 생성합니다.")
    @PostMapping("/posts/{postId}/polls")
    public ResponseEntity<ApiResponse<com.LionKing.Teamply.domain.poll.dto.response.PollResDTO.CreatePollRes>> createPoll(
            @PathVariable Long postId,
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CreatePollReq request
    ) {
        Long pollId = pollCommandService.createPoll(postId, userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "투표가 성공적으로 생성되었습니다.", new com.LionKing.Teamply.domain.poll.dto.response.PollResDTO.CreatePollRes(pollId)));
    }

    @Operation(summary = "투표 상세 조회", description = "투표 정보, 선택지 목록, 그리고 현재 로그인한 유저의 투표 여부를 조회합니다.")
    @GetMapping("/polls/{pollId}")
    public ResponseEntity<ApiResponse<PollDetailRes>> getPollDetail(
            @PathVariable Long pollId,
            @AuthenticationPrincipal Long userId
    ) {
        PollDetailRes response = pollQueryService.getPollDetail(pollId, userId);
        return ResponseEntity.ok(ApiResponse.success(200, "투표 상세 조회 성공", response));
    }

    @Operation(summary = "투표 결과 통계 조회", description = "투표의 총 참여자 수 및 각 선택지별 투표한 사람 목록을 조회합니다.")
    @GetMapping("/polls/{pollId}/results")
    public ResponseEntity<ApiResponse<PollResultRes>> getPollResult(
            @PathVariable Long pollId
    ) {
        PollResultRes response = pollQueryService.getPollResult(pollId);
        return ResponseEntity.ok(ApiResponse.success(200, "투표 결과 통계 조회 성공", response));
    }

    @Operation(summary = "투표하기", description = "주어진 선택지(단일/복수)에 투표합니다. 기존 내역은 덮어씁니다.")
    @PostMapping("/polls/{pollId}/votes")
    public ResponseEntity<ApiResponse<Void>> castVote(
            @PathVariable Long pollId,
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CastVoteReq request
    ) {
        pollCommandService.castVote(pollId, userId, request);
        return ResponseEntity.ok(ApiResponse.success(200, "투표가 성공적으로 반영되었습니다.", null));
    }

    @Operation(summary = "투표 수동 마감", description = "진행 중인 투표를 즉시 마감 상태로 변경합니다.")
    @PatchMapping("/polls/{pollId}/close")
    public ResponseEntity<ApiResponse<Void>> closePoll(
            @PathVariable Long pollId,
            @AuthenticationPrincipal Long userId
    ) {
        pollCommandService.closePoll(pollId, userId);
        return ResponseEntity.ok(ApiResponse.success(200, "투표가 성공적으로 마감되었습니다.", null));
    }

    @Operation(summary = "투표 마감일 수정", description = "진행 중인 투표의 마감 기한을 연장하거나 수정합니다.")
    @PatchMapping("/polls/{pollId}/deadline")
    public ResponseEntity<ApiResponse<Void>> updateDeadline(
            @PathVariable Long pollId,
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody UpdateDeadlineReq request
    ) {
        pollCommandService.updateDeadline(pollId, userId, request);
        return ResponseEntity.ok(ApiResponse.success(200, "투표 마감일이 성공적으로 수정되었습니다.", null));
    }
}
