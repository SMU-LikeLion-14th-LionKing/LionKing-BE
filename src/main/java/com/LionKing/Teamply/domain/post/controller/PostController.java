package com.LionKing.Teamply.domain.post.controller;

import com.LionKing.Teamply.domain.post.dto.request.PostReqDTO;
import com.LionKing.Teamply.domain.post.dto.response.PostResDTO;
import com.LionKing.Teamply.domain.post.service.command.PostCommandService;
import com.LionKing.Teamply.domain.post.service.query.PostQueryService;
import com.LionKing.Teamply.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "게시글 관련 API", description = "게시글(작업,질문,회의록,공지사항)관련 API")
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;

    @PostMapping("projects/{projectId}/notice")
    @Operation(summary = "공지사항 생성", description = "공지사항을 작성합니다.")
    public ApiResponse<PostResDTO.NoticeCreateRes> createNotice(
            @PathVariable Long projectId,
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody PostReqDTO.NoticeCreateReq req
    ) {
        PostResDTO.NoticeCreateRes res = postCommandService.createNotice(projectId, userId, req);
        return ApiResponse.success(201, "공지사항이 생성되었습니다.", res);
    }

    @GetMapping("projects/{projectId}/notice")
    @Operation(summary = "공지사항 목록 조회", description = "프로젝트의 공지사항들을 N개씩 가져옵니다.")
    public ApiResponse<Page<PostResDTO.NoticesGetRes>> getNotices(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<PostResDTO.NoticesGetRes> res = postQueryService.getNotices(projectId, page, size);
        return ApiResponse.success("공지사항 목록 조회 성공", res);
    }

    @GetMapping("projects/{projectId}/notice/recent")
    @Operation(summary = "공지사항 목록 요약 조회", description = "최근 공지사항중 3개를 요약해서 가져옵니다.")
    public ApiResponse<List<PostResDTO.RecentNoticeRes>> getRecentNotices(
            @PathVariable Long projectId
    ) {
        List<PostResDTO.RecentNoticeRes> res = postQueryService.getRecentNotices(projectId);
        return ApiResponse.success("최근 공지사항 조회 성공", res);
    }
}
