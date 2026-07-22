package com.LionKing.Teamply.domain.notification.converter;

import com.LionKing.Teamply.domain.notification.dto.response.NotificationInfoRes;
import com.LionKing.Teamply.domain.notification.dto.response.NotificationPageRes;
import com.LionKing.Teamply.domain.notification.dto.response.NotificationUnreadCountRes;
import com.LionKing.Teamply.domain.notification.entity.Notification;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

public class NotificationConverter {

    public static NotificationInfoRes toNotificationInfoRes(Notification notification) {
        return NotificationInfoRes.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .type(notification.getType())
                .relatedId(notification.getRelatedId())
                .relatedType(notification.getRelatedType())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    public static NotificationPageRes toNotificationPageRes(Slice<Notification> notificationSlice) {
        List<NotificationInfoRes> notificationList = notificationSlice.getContent().stream()
                .map(NotificationConverter::toNotificationInfoRes)
                .collect(Collectors.toList());

        return NotificationPageRes.builder()
                .notifications(notificationList)
                .isFirst(notificationSlice.isFirst())
                .isLast(notificationSlice.isLast())
                .hasNext(notificationSlice.hasNext())
                .currentPage(notificationSlice.getNumber())
                .build();
    }

    public static NotificationUnreadCountRes toNotificationUnreadCountRes(int count) {
        return NotificationUnreadCountRes.builder()
                .unreadCount(count)
                .build();
    }
}
