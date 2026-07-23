package com.LionKing.Teamply.domain.ai.entity;

import com.LionKing.Teamply.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "ai_priority_items")
public class AiPriorityItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ai_briefing_id", nullable = false)
    private AiBriefing aiBriefing;

    @Column(name = "item_rank", nullable = false) // rank는 SQL 예약어일 수 있어 item_rank로 명명
    private Integer rank;

    @Column(nullable = false, length = 255)
    private String content;

    @Column(nullable = false)
    private LocalDateTime deadline;

}
