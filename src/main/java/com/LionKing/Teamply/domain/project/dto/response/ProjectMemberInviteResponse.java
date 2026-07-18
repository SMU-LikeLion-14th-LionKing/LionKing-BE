package com.LionKing.Teamply.domain.project.dto.response;

import com.LionKing.Teamply.domain.project.entity.ProjectMember;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ProjectMemberInviteResponse(
        @JsonProperty("user_id")
        Long userId,
        String name
) {
    public static ProjectMemberInviteResponse from(ProjectMember member) {
        return new ProjectMemberInviteResponse(
                member.getUser().getId(),
                member.getUser().getName()
        );
    }
}