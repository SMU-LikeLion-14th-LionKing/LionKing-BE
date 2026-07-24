package com.LionKing.Teamply.domain.project.dto.response;

import java.time.LocalDateTime;

public class ProjectResDTO {

    public record ProjectCreateRes(
            Long id,
            LocalDateTime createdAt
    ){
    }

    public record ProjectListRes(
            Long id,
            String name
    ){
    }

    public record ProjectSummaryRes(
            Long id,
            String name,
            String title,
            String projectType,
            LocalDateTime deadline,
            Float progressRate,
            Float aiProgressRate,
            Integer totalTaskCount
    ){
    }

    public record ProjectsGetRes(
            String title
    ){
    }
}
