package com.LionKing.Teamply.domain.post.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class PostResDTO {

    public record NoticeCreateRes(
            Long postId,
            LocalDateTime createdAt
    ) {

    }

    public record NoticesGetRes(
            String title,
            String content,
            LocalDateTime createdAt
    ){

    }

    public record RecentNoticeRes(
            Long postId,
            String title,
            LocalDateTime createdAt
    ) {

    }
    
    /*--작업/질문 생성 응답--*/
    public record PostCreateRes(
            Long postId,
            LocalDateTime createdAt
    ) {

    }

    /*--게시글 수정 응답--*/
    public record PostUpdateRes(
            Long postId,
            LocalDateTime updatedAt
    ) {

    }

    /*--게시글 목록조회--*/
    public record PostListItemRes(
            Long postId,
            AuthorRes author,
            String categoryName,
            String title,
            int reactionCount,
            int commentCount,
            LocalDateTime createdAt
    ) {
        public record AuthorRes(Long userId, String name) {}
    }

    /*--게시글 상세조회--*/
    public record PostDetailRes(
            Long postId,
            AuthorRes author,
            String categoryName,
            String title,
            String content,
            List<AttachmentRes> attachments,
            int reactionCount,
            int commentCount,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        public record AuthorRes(Long userId, String name) {}
        public record AttachmentRes(Long attachmentId, String fileUrl, String fileType) {}
    }

    /*--첨부파일 업로드 응답--*/
    public record FileUploadRes(
            Long fileId,
            String fileUrl,
            String fileType
    ) {

    }

    /*--첨부파일 다운로드 URL 응답--*/
    public record FileDownloadUrlRes(
            String downloadUrl
    ) {

    }
}
