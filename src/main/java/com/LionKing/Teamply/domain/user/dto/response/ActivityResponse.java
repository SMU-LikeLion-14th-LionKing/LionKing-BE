package com.LionKing.Teamply.domain.user.dto.response;

import com.LionKing.Teamply.domain.post.entity.Post;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ActivityResponse {

    private String type;
    private String title;

    @JsonProperty("project_name")
    private String projectName;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    public static ActivityResponse fromPost(Post post) {
        return ActivityResponse.builder()
                .type("POST")
                .title(post.getTitle())
                .projectName(post.getProject().getName())
                .createdAt(post.getCreatedAt())
                .build();
    }
}