package com.LionKing.Teamply.domain.notification.repository;

import com.LionKing.Teamply.domain.notification.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // 전체/요약 조회용 (Slice 반환하여 무한스크롤 지원)
    Slice<Notification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    // 안 읽은 알림 개수 조회
    int countByUserIdAndIsReadFalse(Long userId);

    // 알림 전체 읽음 처리 (벌크 연산)
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.user.id = :userId AND n.isRead = false")
    void updateIsReadTrueByUserId(@Param("userId") Long userId);
}
