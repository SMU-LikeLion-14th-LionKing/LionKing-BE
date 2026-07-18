package com.LionKing.Teamply.domain.project.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProjectMemberUpdateRequest(
        String permission,

        @JsonProperty("role_description")
        String roleDescription
) {
}