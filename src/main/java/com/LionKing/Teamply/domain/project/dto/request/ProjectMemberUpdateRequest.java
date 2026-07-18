package com.LionKing.Teamply.domain.project.dto.request;

public record ProjectMemberUpdateRequest(Long userId, String role, String position) {
}