package com.LionKing.Teamply.domain.notification.entity;

import com.LionKing.Teamply.domain.user.entity.User;
import com.LionKing.Teamply.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notifications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationType type;

    @Column(name = "related_id")
    private Long relatedId;

    @Column(name = "related_type", length = 50)
    private String relatedType;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    @Builder
    public Notification(User user, String title, String content, NotificationType type, Long relatedId, String relatedType) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.type = type;
        this.relatedId = relatedId;
        this.relatedType = relatedType;
        this.isRead = false;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}
