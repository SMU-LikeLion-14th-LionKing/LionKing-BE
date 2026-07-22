package com.LionKing.Teamply.domain.notification.service.query;

import com.LionKing.Teamply.domain.notification.dto.response.NotificationPageRes;
import com.LionKing.Teamply.domain.notification.dto.response.NotificationUnreadCountRes;

public interface NotificationQueryService {

    // 알림 목록 조회 (페이징)
    NotificationPageRes getNotifications(Long userId, int page, int size);

    // 알림 목록 요약 조회 (최신 4건)
    NotificationPageRes getNotificationSummary(Long userId);

    // 읽지 않은 알림 개수 조회
    NotificationUnreadCountRes getUnreadCount(Long userId);
}
