package com.LionKing.Teamply.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;

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
}
