package com.LionKing.Teamply.domain.ai.service.query;

import com.LionKing.Teamply.domain.ai.dto.response.AiFeedbackResponse;
import com.LionKing.Teamply.domain.ai.entity.AiFeedback;
import com.LionKing.Teamply.domain.ai.repository.AiFeedbackRepository;
import com.LionKing.Teamply.global.apiPayload.code.GeneralErrorCode;
import com.LionKing.Teamply.global.apiPayload.exception.handler.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiFeedbackQueryServiceImpl implements AiFeedbackQueryService {

    private final AiFeedbackRepository aiFeedbackRepository;

    @Override
    public AiFeedbackResponse.FeedbackPage getMyFeedbacks(Long userId, int page, int size) {
        if (page < 1 || size <= 0) {
            throw new CustomException(GeneralErrorCode.BAD_REQUEST_400);
        }

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<AiFeedback> result = aiFeedbackRepository.findAllByUser_Id(userId, pageable);

        return AiFeedbackResponse.FeedbackPage.builder()
                .content(result.getContent().stream()
                        .map(f -> AiFeedbackResponse.FeedbackItem.builder()
                                .feedbackType(f.getFeedbackType() != null ? f.getFeedbackType().name() : null)
                                .originalContent(f.getOriginalContent())
                                .suggestedContent(f.getSuggestedContent())
                                .build())
                        .toList())
                .page(page)
                .totalPages(result.getTotalPages())
                .build();
    }
}