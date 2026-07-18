package com.LionKing.Teamply.domain.project.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ProjectMemberInviteRequest(
        @NotBlank
        @JsonProperty("team_name")
        String teamName,

        @NotBlank
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email
) {
}