package com.LionKing.Teamply.domain.project.entity;

import com.LionKing.Teamply.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "projects")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name; // 팀명

    @Column(name = "project_type", length = 255)
    private String projectType; // 프로젝트 분야

    @Column(length = 255)
    private String title; // 프로젝트 주제

    private LocalDateTime deadline;

    @Column(name = "progress_rate")
    private Float progressRate;

    @Column(name = "ai_progress_rate")
    private Float aiProgressRate;

    @Builder
    public Project(String name, String projectType, String title, LocalDateTime deadline) {
        this.name = name;
        this.projectType = projectType;
        this.title = title;
        this.deadline = deadline;
        this.progressRate = 0f;
        this.aiProgressRate = 0f;
    }

    public void updateProgressRate(Float progressRate) {
        this.progressRate = progressRate;
    }

    public void updateAiProgressRate(Float aiProgressRate) {
        this.aiProgressRate = aiProgressRate;
    }
}