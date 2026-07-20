package com.LionKing.Teamply.domain.post.service.query;

import com.LionKing.Teamply.domain.post.converter.PostConverter;
import com.LionKing.Teamply.domain.post.dto.response.PostResDTO;
import com.LionKing.Teamply.domain.post.entity.Post;
import com.LionKing.Teamply.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQueryServiceImpl implements PostQueryService {

    private final PostRepository postRepository;

    @Override
    public Page<PostResDTO.NoticesGetRes> getNotices(Long projectId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository
                .findAllByProject_IdAndTypeOrderByCreatedAtDesc(projectId, "공지사항", pageable);

        return posts.map(PostConverter::toNoticesGetRes);
    }

    @Override
    public List<PostResDTO.RecentNoticeRes> getRecentNotices(Long projectId) {
        List<Post> posts = postRepository
                .findTop3ByProject_IdAndTypeOrderByCreatedAtDesc(projectId, "공지사항");

        return posts.stream()
                .map(PostConverter::toRecentNoticeRes)
                .toList();
    }
}
