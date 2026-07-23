package com.LionKing.Teamply.domain.user.service.query;

import com.LionKing.Teamply.domain.user.dto.response.ActivityResponse;

public interface UserActivityQueryService {
    ActivityResponse.ActivityPage getMyActivities(Long userId, int page, int size);
}