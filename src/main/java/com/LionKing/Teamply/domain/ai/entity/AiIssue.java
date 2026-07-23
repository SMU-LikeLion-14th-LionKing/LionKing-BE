package com.LionKing.Teamply.domain.ai.entity;

import com.LionKing.Teamply.domain.project.entity.Project;
import com.LionKing.Teamply.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "ai_issues")
public class AiIssue extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false, length = 100)
    private String category; // 소통오류/일정지연/회의록미해결/댓글참여부족 등

    @Column(name = "risk_level", nullable = false, length = 20)
    private String riskLevel; // 낮음/중간/높음

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String cause;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String suggestion;

}
