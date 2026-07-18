package com.LionKing.Teamply.domain.project.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ProjectLinkCreateRequest(
        @NotBlank(message = "워크스페이스 이름을 입력해주세요.")
        String name,

        @NotBlank(message = "URL을 입력해주세요.")
        String url
) {
}
