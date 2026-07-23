package com.LionKing.Teamply.domain.ai.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class AiFeedbackResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class FeedbackItem {
        @JsonProperty("feedback_type")
        private String feedbackType;

        @JsonProperty("original_content")
        private String originalContent;

        @JsonProperty("suggested_content")
        private String suggestedContent;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class FeedbackPage {
        private List<FeedbackItem> content;
        private int page;

        @JsonProperty("total_pages")
        private int totalPages;
    }
}