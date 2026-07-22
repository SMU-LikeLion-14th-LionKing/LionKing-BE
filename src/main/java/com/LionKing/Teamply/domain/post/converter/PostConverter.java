package com.LionKing.Teamply.domain.post.converter;

import com.LionKing.Teamply.domain.post.dto.request.PostReqDTO;
import com.LionKing.Teamply.domain.post.dto.response.PostResDTO;
import com.LionKing.Teamply.domain.post.entity.Attachment;
import com.LionKing.Teamply.domain.post.entity.Post;
import com.LionKing.Teamply.domain.post.entity.PostType;
import com.LionKing.Teamply.domain.project.entity.Project;
import com.LionKing.Teamply.domain.user.entity.User;

import java.util.List;

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

    /*--작업/질문--*/
    public static Post toTask(PostReqDTO.TaskCreateReq req, Project project, User user) {
        return Post.builder()
                .project(project)
                .user(user)
                .title(req.title())
                .content(req.content())
                .type(PostType.TASK)
                .build();
    }

    public static Post toQuestion(PostReqDTO.QuestionCreateReq req, Project project, User user) {
        return Post.builder()
                .project(project)
                .user(user)
                .title(req.title())
                .content(req.content())
                .type(PostType.QUESTION)
                .build();
    }

    public static PostResDTO.PostCreateRes toPostCreateRes(Post post) {
        return new PostResDTO.PostCreateRes(post.getId(), post.getCreatedAt());
    }

    public static PostResDTO.PostUpdateRes toPostUpdateRes(Post post) {
        return new PostResDTO.PostUpdateRes(post.getId(), post.getUpdatedAt());
    }

    /* --목록/상세-- */
    public static PostResDTO.PostListItemRes toPostListItem(Post post, int reactionCount, int commentCount) {
        return new PostResDTO.PostListItemRes(
                post.getId(),
                new PostResDTO.PostListItemRes.AuthorRes(
                        post.getUser().getId(),
                        post.getUser().getName()
                ),
                post.getType(),
                post.getTitle(),
                reactionCount,
                commentCount,
                post.getCreatedAt()
        );
    }

    public static PostResDTO.PostDetailRes toPostDetail(
            Post post,
            List<Attachment> attachments,
            int reactionCount,
            int commentCount
    ) {
        List<PostResDTO.PostDetailRes.AttachmentRes> attachmentResList = attachments.stream()
                .map(a -> new PostResDTO.PostDetailRes.AttachmentRes(a.getId(), a.getFileUrl(), a.getFileType()))
                .toList();

        return new PostResDTO.PostDetailRes(
                post.getId(),
                new PostResDTO.PostDetailRes.AuthorRes(
                        post.getUser().getId(),
                        post.getUser().getName()
                ),
                post.getType(),
                post.getTitle(),
                post.getContent(),
                attachmentResList,
                reactionCount,
                commentCount,
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }

    /* -- 첨부파일 -- */
    public static Attachment toAttachment(String fileUrl, String fileType, Post post) {
        return Attachment.builder()
                .post(post)
                .fileUrl(fileUrl)
                .fileType(fileType)
                .build();
    }

    public static PostResDTO.FileUploadRes toFileUploadRes(Attachment attachment) {
        return new PostResDTO.FileUploadRes(
                attachment.getId(),
                attachment.getFileUrl(),
                attachment.getFileType()
        );
    }
}

