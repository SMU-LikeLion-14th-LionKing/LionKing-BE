package com.LionKing.Teamply.domain.project.dto.response;

import com.LionKing.Teamply.domain.project.entity.ProjectLink;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ProjectLinkResponse(
        @JsonProperty("link_id")
        Long linkId,
        String name,
        String url
) {
    public static ProjectLinkResponse from(ProjectLink link) {
        return new ProjectLinkResponse(
                link.getId(),
                link.getName(),
                link.getUrl()
        );
    }
}