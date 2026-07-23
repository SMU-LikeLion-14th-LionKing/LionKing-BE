package com.LionKing.Teamply.domain.ai.repository;

import com.LionKing.Teamply.domain.ai.entity.AiFeedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiFeedbackRepository extends JpaRepository<AiFeedback, Long> {
    Page<AiFeedback> findAllByUser_Id(Long userId, Pageable pageable);
}