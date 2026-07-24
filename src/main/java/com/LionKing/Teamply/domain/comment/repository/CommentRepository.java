package com.LionKing.Teamply.domain.comment.repository;

import com.LionKing.Teamply.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 게시글의 댓글 목록 (최신순)
    Page<Comment> findAllByPost_IdOrderByCreatedAtAsc(Long postId, Pageable pageable);

    void deleteAllByPost_Id(Long postId);
}