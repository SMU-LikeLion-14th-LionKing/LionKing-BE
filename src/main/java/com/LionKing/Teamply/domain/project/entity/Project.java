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
    private String teamName; // 팀명

    @Column(name = "project_type", length = 255)
    private String projectType; // 프로젝트 분야

    @Column(length = 255)
    private String title; // 팀페이지

    private LocalDateTime deadline;

    @Column(name = "progress_rate")
    private Float progressRate;

    @Column(name = "ai_progress_rate")
    private Float aiProgressRate;

    @Column(name = "total_task_count")
    private Integer totalTaskCount;

    @Builder
    public Project(String teamName, String projectType, String title, LocalDateTime deadline) {
        this.teamName = teamName;
        this.projectType = projectType;
        this.title = title;
        this.deadline = deadline;
        this.progressRate = 0f;
        this.aiProgressRate = 0f;
        this.totalTaskCount = 0;
    }

    public void updateTotalTaskCount(Integer count) {
        this.totalTaskCount = count;
    }

    public void updateProgressRate(Float progressRate) {
        this.progressRate = progressRate;
    }

    public void updateAiProgressRate(Float aiProgressRate) {
        this.aiProgressRate = aiProgressRate;
    }

    public void calculateProgress(int confirmedTaskCount, int actualTaskCount) {
        float taskProgress = 0f;
        int effectiveTotalTaskCount = 0;

        if (this.totalTaskCount != null && this.totalTaskCount > 0) {
            effectiveTotalTaskCount = this.totalTaskCount;
        }

        if (actualTaskCount > effectiveTotalTaskCount) {
            effectiveTotalTaskCount = actualTaskCount;
        }

        if (effectiveTotalTaskCount > 0) {
            taskProgress = ((float) confirmedTaskCount / effectiveTotalTaskCount) * 100f;
        }

        float scheduleProgress = 0f;
        if (this.getCreatedAt() != null && this.deadline != null) {
            long totalDays = java.time.temporal.ChronoUnit.DAYS.between(this.getCreatedAt().toLocalDate(), this.deadline.toLocalDate());
            long elapsedDays = java.time.temporal.ChronoUnit.DAYS.between(this.getCreatedAt().toLocalDate(), java.time.LocalDate.now());

            if (totalDays > 0) {
                // Ensure elapsedDays doesn't exceed totalDays or fall below 0
                elapsedDays = Math.max(0, Math.min(elapsedDays, totalDays));
                scheduleProgress = ((float) elapsedDays / totalDays) * 100f;
            }
        }

        // 작업 진행도 70%, 일정 진행도 30% 반영
        this.progressRate = (taskProgress * 0.7f) + (scheduleProgress * 0.3f);
    }
}