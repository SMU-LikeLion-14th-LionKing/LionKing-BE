package com.LionKing.Teamply.domain.notification.service.command;

import com.LionKing.Teamply.domain.notification.entity.Notification;
import com.LionKing.Teamply.domain.notification.entity.NotificationType;
import com.LionKing.Teamply.domain.user.entity.User;

public interface NotificationCommandService {
    
    // 알림 단건 읽음 처리
    void readNotification(Long userId, Long notificationId);
    
    // 알림 전체 읽음 처리
    void readAllNotifications(Long userId);
    
    // 내부 서버용 - 알림 생성
    Notification createNotification(User user, String title, String content, NotificationType type, Long relatedId, String relatedType);
}
