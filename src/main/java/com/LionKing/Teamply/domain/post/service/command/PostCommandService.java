package com.LionKing.Teamply.domain.post.service.command;

import com.LionKing.Teamply.domain.post.dto.request.PostReqDTO;
import com.LionKing.Teamply.domain.post.dto.response.PostResDTO;

public interface PostCommandService {

    PostResDTO.NoticeCreateRes createNotice(Long projectId, Long userId, PostReqDTO.NoticeCreateReq req);
}
