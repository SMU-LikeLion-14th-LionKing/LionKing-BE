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
