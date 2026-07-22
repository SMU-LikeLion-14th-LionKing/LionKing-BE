package com.LionKing.Teamply.domain.comment.service.command;

import com.LionKing.Teamply.domain.comment.converter.CommentConverter;
import com.LionKing.Teamply.domain.comment.dto.request.CommentReqDTO;
import com.LionKing.Teamply.domain.comment.dto.response.CommentResDTO;
import com.LionKing.Teamply.domain.comment.entity.Comment;
import com.LionKing.Teamply.domain.comment.exception.CommentErrorCode;
import com.LionKing.Teamply.domain.comment.exception.CommentException;
import com.LionKing.Teamply.domain.comment.repository.CommentRepository;
import com.LionKing.Teamply.domain.post.entity.Post;
import com.LionKing.Teamply.domain.post.exception.PostErrorCode;
import com.LionKing.Teamply.domain.post.exception.PostException;
import com.LionKing.Teamply.domain.post.repository.PostRepository;
import com.LionKing.Teamply.domain.user.entity.User;
import com.LionKing.Teamply.domain.user.repository.UserRepository;
import com.LionKing.Teamply.global.apiPayload.code.GeneralErrorCode;
import com.LionKing.Teamply.global.apiPayload.exception.handler.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentCommandServiceImpl implements CommentCommandService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /*-- 댓글 작성 --*/
    @Override
    public CommentResDTO.CommentCreateRes createComment(Long postId, Long userId, CommentReqDTO.CommentCreateReq req) {
        Post post = findPost(postId);
        User user = findUser(userId);

        Comment comment = CommentConverter.toComment(req, post, user);
        Comment saved = commentRepository.save(comment);

        return CommentConverter.toCommentCreateRes(saved);
    }

    /*-- 댓글 수정 --*/
    @Override
    public CommentResDTO.CommentUpdateRes updateComment(Long commentId, Long userId, CommentReqDTO.CommentUpdateReq req) {
        Comment comment = findComment(commentId);
        validateAuthor(comment, userId);

        comment.updateContent(req.content());
        return CommentConverter.toCommentUpdateRes(comment);
    }

    /*-- 댓글 삭제 --*/
    @Override
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = findComment(commentId);
        validateAuthor(comment, userId);

        commentRepository.delete(comment);
    }


    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));
    }

    private void validateAuthor(Comment comment, Long userId) {
        if (!comment.isAuthor(userId)) {
            throw new CommentException(CommentErrorCode.COMMENT_FORBIDDEN);
        }
    }
}