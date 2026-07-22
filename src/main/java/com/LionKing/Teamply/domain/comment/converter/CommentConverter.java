package com.LionKing.Teamply.domain.comment.converter;

import com.LionKing.Teamply.domain.comment.dto.request.CommentReqDTO;
import com.LionKing.Teamply.domain.comment.dto.response.CommentResDTO;
import com.LionKing.Teamply.domain.comment.entity.Comment;
import com.LionKing.Teamply.domain.post.entity.Post;
import com.LionKing.Teamply.domain.user.entity.User;

public class CommentConverter {

    public static Comment toComment(CommentReqDTO.CommentCreateReq req, Post post, User user) {
        return Comment.builder()
                .post(post)
                .user(user)
                .content(req.content())
                .build();
    }

    public static CommentResDTO.CommentCreateRes toCommentCreateRes(Comment comment) {
        return new CommentResDTO.CommentCreateRes(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }

    public static CommentResDTO.CommentUpdateRes toCommentUpdateRes(Comment comment) {
        return new CommentResDTO.CommentUpdateRes(
                comment.getId(),
                comment.getContent(),
                comment.getUpdatedAt()
        );
    }

    public static CommentResDTO.CommentListItemRes toCommentListItem(Comment comment) {
        return new CommentResDTO.CommentListItemRes(
                comment.getId(),
                new CommentResDTO.CommentListItemRes.AuthorRes(
                        comment.getUser().getId(),
                        comment.getUser().getName()
                ),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}