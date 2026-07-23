package com.LionKing.Teamply.domain.post.dto.request;

import com.LionKing.Teamply.domain.post.entity.ReactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReactionRequest {
    @NotNull(message = "반응 타입을 입력해주세요.")
    @Schema(description = "반응 종류", example = "CONFIRMED")
    private ReactionType reactionType;
}
