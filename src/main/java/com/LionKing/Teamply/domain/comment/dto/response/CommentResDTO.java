package com.LionKing.Teamply.domain.comment.dto.response;

import java.time.LocalDateTime;

public class CommentResDTO {

    public record CommentCreateRes(
            Long commentId,
            String content,
            LocalDateTime createdAt
    ) {}

    public record CommentUpdateRes(
            Long commentId,
            String content,
            LocalDateTime updatedAt
    ) {}

    public record CommentListItemRes(
            Long commentId,
            AuthorRes author,
            String content,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        public record AuthorRes(Long userId, String name) {}
    }
}