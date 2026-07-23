package com.LionKing.Teamply.domain.poll.entity;

import com.LionKing.Teamply.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "poll_options")
public class PollOption extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;

    @Column(nullable = false, length = 255)
    private String content;

    @Column(name = "image_url", length = 512)
    private String imageUrl;

    protected void setPoll(Poll poll) {
        this.poll = poll;
    }
}
