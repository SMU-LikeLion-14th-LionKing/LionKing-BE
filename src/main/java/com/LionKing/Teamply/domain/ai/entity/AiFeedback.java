package com.LionKing.Teamply.domain.ai.entity;

import com.LionKing.Teamply.domain.user.entity.User;
import com.LionKing.Teamply.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ai_feedback")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AiFeedback extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "feedback_type", nullable = false, length = 50)
    private FeedbackType feedbackType;

    @Column(name = "original_content", columnDefinition = "TEXT", nullable = false)
    private String originalContent;

    @Column(name = "suggested_content", columnDefinition = "TEXT", nullable = false)
    private String suggestedContent;
}