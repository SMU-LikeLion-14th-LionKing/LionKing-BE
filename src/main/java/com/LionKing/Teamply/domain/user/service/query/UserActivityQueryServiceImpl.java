package com.LionKing.Teamply.domain.user.service.query;

import com.LionKing.Teamply.domain.post.entity.Post;
import com.LionKing.Teamply.domain.post.repository.PostRepository;
import com.LionKing.Teamply.domain.user.dto.response.ActivityResponse;
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
public class UserActivityQueryServiceImpl implements UserActivityQueryService {

    private final PostRepository postRepository;

    @Override
    public ActivityResponse.ActivityPage getMyActivities(Long userId, int page, int size) {
        if (page < 1 || size <= 0) {
            throw new CustomException(GeneralErrorCode.BAD_REQUEST_400);
        }

        // ... existing code ...
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        // 사용자가 작성한 게시글을 활동으로 조회
        Page<Post> posts = postRepository.findAllByUser_Id(userId, pageable);

        return ActivityResponse.ActivityPage.builder()
                .content(posts.getContent().stream()
                        .map(p -> ActivityResponse.ActivityItem.builder()
                                .type(p.getType() != null ? p.getType() : "POST")
                                .title(p.getTitle())
                                .projectName(p.getProject() != null ? p.getProject().getName() : null)
                                .createdAt(p.getCreatedAt())
                                .build())
                        .toList())
                .page(page)
                .totalPages(posts.getTotalPages())
                .build();
    }
}