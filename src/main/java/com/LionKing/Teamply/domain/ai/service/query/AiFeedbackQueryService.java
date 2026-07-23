package com.LionKing.Teamply.domain.ai.service.query;

import com.LionKing.Teamply.domain.ai.dto.response.AiFeedbackResponse;

public interface AiFeedbackQueryService {
    AiFeedbackResponse.FeedbackPage getMyFeedbacks(Long userId, int page, int size);
}