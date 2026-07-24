package com.LionKing.Teamply.domain.project.dto.response;

import com.LionKing.Teamply.domain.project.entity.Project;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ProjectSelectResponse(
        @JsonProperty("project_id")
        Long projectId,

        @JsonProperty("team_name")
        String teamName
) {
    public static ProjectSelectResponse from(Project project) {
        return new ProjectSelectResponse(project.getId(), project.getTeamName());
    }
}