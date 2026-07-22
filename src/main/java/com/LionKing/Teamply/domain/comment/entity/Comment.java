package com.LionKing.Teamply.domain.comment.entity;

import com.LionKing.Teamply.domain.post.entity.Post;
import com.LionKing.Teamply.domain.user.entity.User;
import com.LionKing.Teamply.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "comments")
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /*-- 내용 수정 --*/
    public void updateContent(String content) {
        if (content != null && !content.isBlank()) {
            this.content = content;
        }
    }

    /*-- 작성자 검증 --*/
    public boolean isAuthor(Long userId) {
        return this.user != null && this.user.getId().equals(userId);
    }
}