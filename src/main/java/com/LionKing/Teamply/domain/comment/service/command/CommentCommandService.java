package com.LionKing.Teamply.domain.comment.service.command;

import com.LionKing.Teamply.domain.comment.dto.request.CommentReqDTO;
import com.LionKing.Teamply.domain.comment.dto.response.CommentResDTO;

public interface CommentCommandService {

    CommentResDTO.CommentCreateRes createComment(Long postId, Long userId, CommentReqDTO.CommentCreateReq req);

    CommentResDTO.CommentUpdateRes updateComment(Long commentId, Long userId, CommentReqDTO.CommentUpdateReq req);

    void deleteComment(Long commentId, Long userId);
}