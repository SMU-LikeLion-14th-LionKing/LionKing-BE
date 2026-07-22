package com.LionKing.Teamply.domain.post.service.command;

import com.LionKing.Teamply.domain.post.dto.request.ReactionRequest;
import com.LionKing.Teamply.domain.post.entity.Post;
import com.LionKing.Teamply.domain.post.entity.PostReaction;
import com.LionKing.Teamply.domain.post.exception.PostErrorCode;
import com.LionKing.Teamply.domain.post.exception.PostException;
import com.LionKing.Teamply.domain.post.repository.PostReactionRepository;
import com.LionKing.Teamply.domain.post.repository.PostRepository;
import com.LionKing.Teamply.domain.user.entity.User;
import com.LionKing.Teamply.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReactionCommandService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostReactionRepository postReactionRepository;

    public void putReaction(Long userId, Long postId, ReactionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        Optional<PostReaction> existingReaction = postReactionRepository.findByPostAndUser(post, user);

        if (existingReaction.isPresent()) {
            existingReaction.get().updateReaction(request.getReactionType());
        } else {
            PostReaction newReaction = PostReaction.builder()
                    .post(post)
                    .user(user)
                    .reactionType(request.getReactionType())
                    .build();
            postReactionRepository.save(newReaction);
        }

        // 진행률 갱신 로직 (PostType이 '작업'인 경우)
        if ("작업".equals(post.getType())) {
            com.LionKing.Teamply.domain.project.entity.Project project = post.getProject();
            int confirmedCount = postRepository.countConfirmedTasksByProjectId(project.getId());
            project.calculateProgress(confirmedCount);
        }
    }
}
