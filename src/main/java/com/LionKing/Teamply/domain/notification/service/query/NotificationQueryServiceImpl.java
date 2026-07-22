package com.LionKing.Teamply.domain.notification.service.query;

import com.LionKing.Teamply.domain.notification.converter.NotificationConverter;
import com.LionKing.Teamply.domain.notification.dto.response.NotificationPageRes;
import com.LionKing.Teamply.domain.notification.dto.response.NotificationUnreadCountRes;
import com.LionKing.Teamply.domain.notification.entity.Notification;
import com.LionKing.Teamply.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationQueryServiceImpl implements NotificationQueryService {

    private final NotificationRepository notificationRepository;

    @Override
    public NotificationPageRes getNotifications(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Notification> notificationSlice = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return NotificationConverter.toNotificationPageRes(notificationSlice);
    }

    @Override
    public NotificationPageRes getNotificationSummary(Long userId) {
        // 요약 조회는 페이지 0, 사이즈 4 고정
        Pageable pageable = PageRequest.of(0, 4);
        Slice<Notification> notificationSlice = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return NotificationConverter.toNotificationPageRes(notificationSlice);
    }

    @Override
    public NotificationUnreadCountRes getUnreadCount(Long userId) {
        int count = notificationRepository.countByUserIdAndIsReadFalse(userId);
        return NotificationConverter.toNotificationUnreadCountRes(count);
    }
}
