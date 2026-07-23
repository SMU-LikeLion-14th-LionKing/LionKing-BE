package com.LionKing.Teamply.domain.post.repository;

import com.LionKing.Teamply.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    //이건 공지사항 목록 최신순으로 받아오는 쿼리문입니다.
    Page<Post> findAllByProject_IdAndTypeOrderByCreatedAtDesc(
            Long projectId, String type, Pageable pageable
    );

    //이건 공지사항 목록 최신순으로 3개 받아오는 쿼리문입니당
    List<Post> findTop3ByProject_IdAndTypeOrderByCreatedAtDesc(
            Long projectId, String type
    );

    //프로젝트 내의 모든 게시물 가져옵니당
    Page<Post> findAllByProject_IdOrderByCreatedAtDesc(
            Long projectId, Pageable pageable
    );

    Page<Post> findAllByUser_Id(Long userId, Pageable pageable);


    @org.springframework.data.jpa.repository.Query("SELECT COUNT(DISTINCT p.id) FROM Post p JOIN PostReaction pr ON p.id = pr.post.id " +
           "WHERE p.project.id = :projectId AND p.type = '작업' AND pr.reactionType = 'CONFIRMED'")
    int countConfirmedTasksByProjectId(@org.springframework.data.repository.query.Param("projectId") Long projectId);
}
