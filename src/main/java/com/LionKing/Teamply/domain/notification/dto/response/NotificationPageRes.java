package com.LionKing.Teamply.domain.notification.dto.response;

import lombok.Builder;
import java.util.List;

@Builder
public record NotificationPageRes(
        List<NotificationInfoRes> notifications,
        boolean isFirst,
        boolean isLast,
        boolean hasNext,
        int currentPage
) {}
