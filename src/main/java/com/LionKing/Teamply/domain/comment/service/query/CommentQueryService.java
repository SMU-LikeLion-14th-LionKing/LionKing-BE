package com.LionKing.Teamply.domain.comment.service.query;

import com.LionKing.Teamply.domain.comment.dto.response.CommentResDTO;
import org.springframework.data.domain.Page;

public interface CommentQueryService {

    Page<CommentResDTO.CommentListItemRes> getComments(Long postId, int page, int size);
}