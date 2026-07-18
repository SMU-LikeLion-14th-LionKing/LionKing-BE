package com.LionKing.Teamply.domain.project.dto.response;

import com.LionKing.Teamply.domain.project.entity.ProjectMember;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record ProjectMemberListResponse(
        @JsonProperty("user_id")
        Long userId,
        String name,
        String email,
        String permission,

        @JsonProperty("role_description")
        String roleDescription,

        @JsonProperty("joined_at")
        LocalDateTime joinedAt
) {
    public static ProjectMemberListResponse from(ProjectMember member) {
        return new ProjectMemberListResponse(
                member.getUser().getId(),
                member.getUser().getName(),
                member.getUser().getEmail(),
                member.getRole().name(),
                member.getPosition(),
                member.getJoinedAt()
        );
    }
}