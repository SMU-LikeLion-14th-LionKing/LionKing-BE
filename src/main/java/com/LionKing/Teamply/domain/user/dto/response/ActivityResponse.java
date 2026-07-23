package com.LionKing.Teamply.domain.user.dto.response;

import com.LionKing.Teamply.domain.post.entity.Post;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ActivityResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ActivityItem {
        private String type;               // "POST", "COMMENT" 등
        private String title;

        @JsonProperty("project_name")
        private String projectName;

        @JsonProperty("created_at")
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ActivityPage {
        private List<ActivityItem> content;
        private int page;                  // 1-based

        @JsonProperty("total_pages")
        private int totalPages;
    }
}