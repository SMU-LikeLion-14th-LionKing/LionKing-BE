package com.LionKing.Teamply.domain.post.entity;

import com.LionKing.Teamply.domain.meeting.entity.MeetingMinute;
import com.LionKing.Teamply.domain.project.entity.Project;
import com.LionKing.Teamply.domain.user.entity.User;
import com.LionKing.Teamply.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    private Project project;

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

    /*-- 자식 엔티티 (Cascade 삭제용) --*/
    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingMinute> meetingMinutes = new ArrayList<>();

    /*--제목/본문 수정 --*/
    public void updateContent(String title, String content) {
        if (title != null && !title.isBlank()) {
            this.title = title;
        }
        if (content != null) {
            this.content = content;
        }
    }

    /*--작성자 검증 --*/
    public boolean isAuthor(Long userId) {
        return this.user != null && this.user.getId().equals(userId);
    }
}