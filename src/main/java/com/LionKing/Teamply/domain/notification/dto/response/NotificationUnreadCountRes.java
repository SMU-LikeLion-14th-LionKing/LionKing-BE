package com.LionKing.Teamply.domain.notification.dto.response;

import lombok.Builder;

@Builder
public record NotificationUnreadCountRes(
        int unreadCount
) {}
