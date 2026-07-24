package com.LionKing.Teamply.domain.post.repository;

import com.LionKing.Teamply.domain.post.entity.Post;
import com.LionKing.Teamply.domain.post.entity.PostReaction;
import com.LionKing.Teamply.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {
    Optional<PostReaction> findByPostAndUser(Post post, User user);
    int countByPostIdAndReactionType(Long postId, com.LionKing.Teamply.domain.post.entity.ReactionType reactionType);
}
