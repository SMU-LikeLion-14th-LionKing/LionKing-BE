package com.LionKing.Teamply.domain.project.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "project_links")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(length = 255)
    private String name; // 워크스페이스 이름 (피그마, 노션 등)

    @Column(length = 255)
    private String url;

    @Builder
    public ProjectLink(Project project, String name, String url) {
        this.project = project;
        this.name = name;
        this.url = url;
    }
}