package com.LionKing.Teamply.domain.project.dto.response;

import java.time.LocalDateTime;

public class ProjectResDTO {

    public record ProjectCreateRes(
            Long id,
            LocalDateTime createdAt
    ){
    }

    public record ProjectGetRes(
            Long id,
            String name,
            String title,
            LocalDateTime deadline
    ){
    }

    public record ProjectsGetRes(
            String title
    ){
    }
}
