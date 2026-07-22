package com.LionKing.Teamply.domain.poll.entity;

import com.LionKing.Teamply.domain.post.entity.Post;
import com.LionKing.Teamply.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "polls")
public class Poll extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String question;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PollStatus status;

    @Column(name = "is_multiple_choice", nullable = false)
    private Boolean isMultipleChoice;

    @Builder.Default
    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PollOption> options = new ArrayList<>();

    public void addOption(PollOption option) {
        options.add(option);
        option.setPoll(this);
    }

    public void updateDeadline(LocalDateTime newDeadline) {
        this.deadline = newDeadline;
        if (this.status == PollStatus.CLOSED && LocalDateTime.now().isBefore(newDeadline)) {
            this.status = PollStatus.ONGOING;
        }
    }

    public void closePoll() {
        this.status = PollStatus.CLOSED;
    }
}
