package com.LionKing.Teamply.domain.project.entity;

import com.LionKing.Teamply.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "project_members")
@Getter
@IdClass(ProjectMemberId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectMember {

    public static final String ROLE_LEADER = "팀장";
    public static final String ROLE_MEMBER = "팀원";

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(length = 255)
    private String role; // 팀장 / 팀원

    @Column(length = 255)
    private String position; // 팀원이 직접 입력하는 역할

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @Builder
    public ProjectMember(User user, Project project, String role) {
        this.user = user;
        this.project = project;
        this.role = role;
        this.joinedAt = LocalDateTime.now();
    }

    public boolean isLeader() {
        return ROLE_LEADER.equals(this.role);
    }

    public void updateRole(String role) {
        this.role = role;
    }

    public void updatePosition(String position) {
        this.position = position;
    }
}