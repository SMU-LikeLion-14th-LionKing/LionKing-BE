package com.LionKing.Teamply.domain.meeting.entity;

import com.LionKing.Teamply.domain.post.entity.Post;
import com.LionKing.Teamply.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "meeting_minutes")
public class MeetingMinute extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "meeting_title", nullable = false)
    private String meetingTitle;

    @Column(name = "meeting_date", nullable = false)
    private LocalDateTime meetingDate;

    @Column(name = "raw_content", columnDefinition = "TEXT")
    private String rawContent;

    @Column(name = "discussion", columnDefinition = "TEXT")
    private String discussion;

    @Column(name = "undecided_items", columnDefinition = "TEXT")
    private String undecidedItems;

    public void updateMeeting(String meetingTitle, LocalDateTime meetingDate, String rawContent) {
        this.meetingTitle = meetingTitle;
        this.meetingDate = meetingDate;
        this.rawContent = rawContent;
    }

    public void updateAiAnalysis(String discussion, String undecidedItems) {
        this.discussion = discussion;
        this.undecidedItems = undecidedItems;
    }
}
