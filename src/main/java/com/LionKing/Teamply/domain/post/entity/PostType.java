package com.LionKing.Teamply.domain.post.entity;

public final class PostType {

    public static final String NOTICE = "공지사항";
    public static final String TASK = "작업";
    public static final String QUESTION = "질문";

    private PostType() {}

    public static String fromCategoryId(Long categoryId) {
        if (categoryId == null) return null;
        if (categoryId == 1L) return TASK;
        if (categoryId == 2L) return QUESTION;
        return null;
    }
}

