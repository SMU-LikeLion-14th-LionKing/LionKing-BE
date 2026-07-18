package com.LionKing.Teamply.domain.project.dto.response;

import com.LionKing.Teamply.domain.project.entity.ProjectLink;

public record ProjectLinkResponse(Long id, String name, String url) {
    public static ProjectLinkResponse from(ProjectLink link) {
        return new ProjectLinkResponse(link.getId(), link.getName(), link.getUrl());
    }
}