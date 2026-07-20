package com.LionKing.Teamply.domain.post.service.query;

import com.LionKing.Teamply.domain.post.dto.response.PostResDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostQueryService {

    Page<PostResDTO.NoticesGetRes> getNotices(Long projectId, int page, int size);

    List<PostResDTO.RecentNoticeRes> getRecentNotices(Long projectId);
}
