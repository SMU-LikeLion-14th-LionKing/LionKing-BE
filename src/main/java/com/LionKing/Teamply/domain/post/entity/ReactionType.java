package com.LionKing.Teamply.domain.post.entity;

public enum ReactionType {
    CONFIRMED("확인 완료"),
    REVIEWING("검토 중");

    private final String description;

    ReactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
