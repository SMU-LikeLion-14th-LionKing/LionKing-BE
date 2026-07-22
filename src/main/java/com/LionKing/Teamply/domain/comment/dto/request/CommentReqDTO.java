package com.LionKing.Teamply.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CommentReqDTO {

    public record CommentCreateReq(
            @NotBlank(message = "댓글 내용을 입력해주세요.")
            String content
    ) {}

    public record CommentUpdateReq(
            @NotBlank(message = "수정할 댓글 내용을 입력해주세요.")
            String content
    ) {}
}