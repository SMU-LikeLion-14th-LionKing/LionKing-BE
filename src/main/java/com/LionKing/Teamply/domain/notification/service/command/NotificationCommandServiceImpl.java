package com.LionKing.Teamply.domain.notification.service.command;

import com.LionKing.Teamply.domain.notification.entity.Notification;
import com.LionKing.Teamply.domain.notification.entity.NotificationType;
import com.LionKing.Teamply.domain.notification.exception.NotificationErrorCode;
import com.LionKing.Teamply.domain.notification.exception.NotificationException;
import com.LionKing.Teamply.domain.notification.repository.NotificationRepository;
import com.LionKing.Teamply.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationCommandServiceImpl implements NotificationCommandService {

    private final NotificationRepository notificationRepository;

    @Override
    public void readNotification(Long userId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationException(NotificationErrorCode.NOTIFICATION_NOT_FOUND));

        if (!notification.getUser().getId().equals(userId)) {
            throw new NotificationException(NotificationErrorCode.NOTIFICATION_FORBIDDEN);
        }

        notification.markAsRead();
    }

    @Override
    public void readAllNotifications(Long userId) {
        notificationRepository.updateIsReadTrueByUserId(userId);
    }

    @Override
    public Notification createNotification(User user, String title, String content, NotificationType type, Long relatedId, String relatedType) {
        Notification notification = Notification.builder()
                .user(user)
                .title(title)
                .content(content)
                .type(type)
                .relatedId(relatedId)
                .relatedType(relatedType)
                .build();
        return notificationRepository.save(notification);
    }
}
