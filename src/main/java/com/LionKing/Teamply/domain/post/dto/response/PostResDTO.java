package com.LionKing.Teamply.domain.post.dto.response;

import java.time.LocalDateTime;

public class PostResDTO {

    public record NoticeCreateRes(
            Long postId,
            LocalDateTime createdAt
    ) {

    }

    public record NoticesGetRes(
            String title,
            String content,
            LocalDateTime createdAt
    ){

    }

    public record RecentNoticeRes(
            Long postId,
            String title,
            LocalDateTime createdAt
    ) {

    }
}
