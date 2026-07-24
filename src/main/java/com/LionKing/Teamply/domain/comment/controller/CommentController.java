package com.LionKing.Teamply.domain.comment.controller;

import com.LionKing.Teamply.domain.comment.dto.request.CommentReqDTO;
import com.LionKing.Teamply.domain.comment.dto.response.CommentResDTO;
import com.LionKing.Teamply.domain.comment.service.command.CommentCommandService;
import com.LionKing.Teamply.domain.comment.service.query.CommentQueryService;
import com.LionKing.Teamply.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@io.swagger.v3.oas.annotations.responses.ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "COMMENT404_1: 존재하지 않거나 이미 삭제된 댓글입니다.", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "COMMENT403_1: 본인이 작성한 댓글만 수정할 수 있습니다.", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "COMMENT400_1: 댓글 내용을 입력해주세요.", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "COMMENT400_2: 수정할 댓글 내용을 입력해주세요.", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "COMMENT400_3: 잘못된 페이징 파라미터입니다.", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class)))
})
@RestController
@Tag(name = "댓글 관련 API", description = "댓글 작성/수정/삭제/조회 API")
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentCommandService commentCommandService;
    private final CommentQueryService commentQueryService;

    @PostMapping("/posts/{postId}/comments")
    @Operation(summary = "댓글 작성")
    public ApiResponse<CommentResDTO.CommentCreateRes> createComment(
            @PathVariable Long postId,
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CommentReqDTO.CommentCreateReq req
    ) {
        return ApiResponse.success(201, "댓글이 등록되었습니다.",
                commentCommandService.createComment(postId, userId, req));
    }

    @PatchMapping("/comments/{commentId}")
    @Operation(summary = "댓글 수정")
    public ApiResponse<CommentResDTO.CommentUpdateRes> updateComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CommentReqDTO.CommentUpdateReq req
    ) {
        return ApiResponse.success(200, "댓글이 수정되었습니다.",
                commentCommandService.updateComment(commentId, userId, req));
    }

    @DeleteMapping("/comments/{commentId}")
    @Operation(summary = "댓글 삭제")
    public ApiResponse<Void> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal Long userId
    ) {
        commentCommandService.deleteComment(commentId, userId);
        return ApiResponse.success("댓글이 삭제되었습니다.");
    }

    @GetMapping("/posts/{postId}/comments")
    @Operation(summary = "댓글 목록 조회")
    public ApiResponse<Page<CommentResDTO.CommentListItemRes>> getComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.success("댓글 목록 조회 성공",
                commentQueryService.getComments(postId, page, size));
    }
}