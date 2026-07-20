package com.LionKing.Teamply.domain.post.converter;

import com.LionKing.Teamply.domain.post.dto.request.PostReqDTO;
import com.LionKing.Teamply.domain.post.dto.response.PostResDTO;
import com.LionKing.Teamply.domain.post.entity.Post;
import com.LionKing.Teamply.domain.project.entity.Project;
import com.LionKing.Teamply.domain.user.entity.User;

public class PostConverter {

    public static Post toNotice(PostReqDTO.NoticeCreateReq req, Project project, User user) {
        return Post.builder()
                .project(project)
                .user(user)
                .title(req.title())
                .content(req.content())
                .type("공지사항")
                .build();
    }

    public static PostResDTO.NoticeCreateRes toNoticeCreateRes(Post post) {
        return new PostResDTO.NoticeCreateRes(
                post.getId(),
                post.getCreatedAt()
        );
    }

    public static PostResDTO.NoticesGetRes toNoticesGetRes(Post post) {
        return new PostResDTO.NoticesGetRes(
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt()
        );
    }

    public static PostResDTO.RecentNoticeRes toRecentNoticeRes(Post post) {
        return new PostResDTO.RecentNoticeRes(
                post.getId(),
                post.getTitle(),
                post.getCreatedAt()
        );
    }
}
