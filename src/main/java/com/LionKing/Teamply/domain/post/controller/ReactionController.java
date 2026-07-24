package com.LionKing.Teamply.domain.post.controller;

import com.LionKing.Teamply.domain.post.dto.request.ReactionRequest;
import com.LionKing.Teamply.domain.post.service.command.ReactionCommandService;
import com.LionKing.Teamply.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Reaction", description = "게시글 반응 API")
@io.swagger.v3.oas.annotations.responses.ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "생성 성공", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "POST404_1: 게시글을 찾을 수 없습니다.", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "POST403_1: 게시글에 대한 권한이 없습니다.", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "POST400_1: 유효하지 않은 게시글 타입입니다.", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "POST404_2: 첨부파일을 찾을 수 없습니다.", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "POST500_1: 파일 업로드에 실패했습니다.", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class)))
})
@RestController
@RequestMapping("/api/posts/{postId}/reactions")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionCommandService reactionCommandService;

    @Operation(summary = "게시글 반응 추가 및 수정", description = "게시글에 대한 반응(확인 완료, 검토 중)을 추가하거나 덮어씌웁니다.")
    @PutMapping
    public ApiResponse<Void> putReaction(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId,
            @Valid @RequestBody ReactionRequest request
    ) {
        reactionCommandService.putReaction(userId, postId, request);
        return ApiResponse.success("반응이 성공적으로 적용되었습니다.");
    }
}
