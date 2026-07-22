package com.LionKing.Teamply.domain.notification.dto.response;

import com.LionKing.Teamply.domain.notification.entity.NotificationType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationInfoRes(
        Long id,
        String title,
        String content,
        NotificationType type,
        Long relatedId,
        String relatedType,
        boolean isRead,
        LocalDateTime createdAt
) {}
