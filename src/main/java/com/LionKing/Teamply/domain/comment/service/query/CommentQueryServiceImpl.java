package com.LionKing.Teamply.domain.comment.service.query;

import com.LionKing.Teamply.domain.comment.converter.CommentConverter;
import com.LionKing.Teamply.domain.comment.dto.response.CommentResDTO;
import com.LionKing.Teamply.domain.comment.entity.Comment;
import com.LionKing.Teamply.domain.comment.exception.CommentErrorCode;
import com.LionKing.Teamply.domain.comment.exception.CommentException;
import com.LionKing.Teamply.domain.comment.repository.CommentRepository;
import com.LionKing.Teamply.domain.post.exception.PostErrorCode;
import com.LionKing.Teamply.domain.post.exception.PostException;
import com.LionKing.Teamply.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentQueryServiceImpl implements CommentQueryService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Override
    public Page<CommentResDTO.CommentListItemRes> getComments(Long postId, int page, int size) {
        if (page < 0 || size <= 0) {
            throw new CommentException(CommentErrorCode.INVALID_PAGE_PARAM);
        }
        if (!postRepository.existsById(postId)) {
            throw new PostException(PostErrorCode.POST_NOT_FOUND);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> comments = commentRepository.findAllByPost_IdOrderByCreatedAtAsc(postId, pageable);
        return comments.map(CommentConverter::toCommentListItem);
    }
}