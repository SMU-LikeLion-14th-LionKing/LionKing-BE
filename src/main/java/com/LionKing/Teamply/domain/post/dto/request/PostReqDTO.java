package com.LionKing.Teamply.domain.post.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PostReqDTO {

    public record NoticeCreateReq(

            @NotBlank(message = "공지 제목을 입력해주세요.")
            String title,

            @NotBlank(message = "공지 내용을 입력해주세요.")
            String content,
            List<AttachmentDto> attachments
    ) {
        public record AttachmentDto(

                @NotBlank(message = "파일 URL은 필수입니다.")
                String fileUrl,

                @NotBlank(message = "파일 타입(종류)은 필수입니다.")
                String fileType
        ) {}
    }

    /*--작업게시글--*/
    public record TaskCreateReq(
            @NotNull(message = "카테고리 ID는 필수입니다.")
            Long categoryId,

            @NotBlank(message = "제목을 입력해주세요.")
            String title,

            @NotBlank(message = "내용을 입력해주세요.")
            String content,

            @Valid List<AttachmentDto> attachments
    ) {
        public record AttachmentDto(
                @NotBlank String fileUrl,
                @NotBlank String fileType
        ) {}
    }

    /*--질문게시글--*/
    public record QuestionCreateReq(
            @NotNull(message = "카테고리 ID는 필수입니다.")
            Long categoryId,

            @NotBlank(message = "제목을 입력해주세요.")
            String title,

            @NotBlank(message = "내용을 입력해주세요.")
            String content,

            @Valid List<AttachmentDto> attachments
    ) {
        public record AttachmentDto(
                @NotBlank String fileUrl,
                @NotBlank String fileType
        ) {}
    }

    /*--게시글 수정(작업/질문 공통)--*/
    public record PostUpdateReq(
            @NotBlank(message = "제목을 입력해주세요.")
            String title,

            @NotBlank(message = "내용을 입력해주세요.")
            String content,

            @Valid List<AttachmentDto> attachments
    ) {
        public record AttachmentDto(
                String fileUrl,
                String fileType
        ) {}
    }
}
