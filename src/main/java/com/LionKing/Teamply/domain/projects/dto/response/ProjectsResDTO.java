package com.LionKing.Teamply.domain.projects.dto.response;

import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public class ProjectsResDTO {

    public record ProjectCreateRes(
            Long id,
            LocalDateTime createdAt
    ){

    }

    public record ProjectGetRes(
            String title,
            LocalDateTime deadline
    ){

    }

    public record ProjectsGEtRes(
            String title
    ){

    }
}

