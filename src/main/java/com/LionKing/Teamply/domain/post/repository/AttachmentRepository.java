package com.LionKing.Teamply.domain.post.repository;

import com.LionKing.Teamply.domain.post.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    
    List<Attachment> findAllByPost_Id(Long postId);

    void deleteAllByPost_Id(Long postId);
}
