package com.LionKing.Teamply.domain.post.service.query;

import com.LionKing.Teamply.domain.post.converter.PostConverter;
import com.LionKing.Teamply.domain.post.dto.response.PostResDTO;
import com.LionKing.Teamply.domain.post.entity.Attachment;
import com.LionKing.Teamply.domain.post.entity.Post;
import com.LionKing.Teamply.domain.post.entity.PostType;
import com.LionKing.Teamply.domain.post.exception.PostErrorCode;
import com.LionKing.Teamply.domain.post.exception.PostException;
import com.LionKing.Teamply.domain.post.repository.AttachmentRepository;
import com.LionKing.Teamply.domain.post.repository.PostRepository;
import com.LionKing.Teamply.domain.project.repository.ProjectRepository;
import com.LionKing.Teamply.domain.project.exception.ProjectErrorCode;
import com.LionKing.Teamply.domain.project.exception.ProjectException;
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
    private final ProjectRepository projectRepository;
    private final AttachmentRepository attachmentRepository;

    @Override
    public Page<PostResDTO.NoticesGetRes> getNotices(Long projectId, int page, int size) {
        if (!projectRepository.existsById(projectId)) {
            throw new ProjectException(ProjectErrorCode.PROJECT_NOT_FOUND);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository
                .findAllByProject_IdAndTypeOrderByCreatedAtDesc(projectId, PostType.NOTICE, pageable);

        return posts.map(PostConverter::toNoticesGetRes);
    }

    @Override
    public List<PostResDTO.RecentNoticeRes> getRecentNotices(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ProjectException(ProjectErrorCode.PROJECT_NOT_FOUND);
        }
        List<Post> posts = postRepository
                .findTop3ByProject_IdAndTypeOrderByCreatedAtDesc(projectId, PostType.NOTICE);

        return posts.stream()
                .map(PostConverter::toRecentNoticeRes)
                .toList();
    }

    @Override
    public Page<PostResDTO.PostListItemRes> getPosts(Long projectId, Long categoryId, int page, int size) {
        if (!projectRepository.existsById(projectId)) {
            throw new ProjectException(ProjectErrorCode.PROJECT_NOT_FOUND);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts;
        if (categoryId == null) {
            posts = postRepository.findAllByProject_IdOrderByCreatedAtDesc(projectId, pageable);
        } else {
            String type = PostType.fromCategoryId(categoryId);
            if (type == null) {
                throw new PostException(PostErrorCode.POST_INVALID_TYPE);
            }
            posts = postRepository.findAllByProject_IdAndTypeOrderByCreatedAtDesc(projectId, type, pageable);
        }
        // reactionCount/commentCount 은 도메인 미구현이라 0으로 응답
        return posts.map(p -> PostConverter.toPostListItem(p, 0, 0));
    }

    @Override
    public PostResDTO.PostDetailRes getPostDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
        List<Attachment> attachments = attachmentRepository.findAllByPost_Id(postId);
        return PostConverter.toPostDetail(post, attachments, 0, 0);
    }
}
