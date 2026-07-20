package com.LionKing.Teamply.domain.post.entity;

import com.LionKing.Teamply.domain.projects.entity.Projects;
import com.LionKing.Teamply.domain.user.entity.User;
import com.LionKing.Teamply.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "posts")
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Projects project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 255)
    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    @Lob
    @Column(name = "original_content", columnDefinition = "TEXT")
    private String originalContent;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(name = "progress_rate")
    private Float progressRate;

    @Column(name = "external_source", length = 100)
    private String externalSource;

    @Column(name = "external_type", length = 50)
    private String externalType;

    @Column(name = "external_url", length = 512)
    private String externalUrl;
}