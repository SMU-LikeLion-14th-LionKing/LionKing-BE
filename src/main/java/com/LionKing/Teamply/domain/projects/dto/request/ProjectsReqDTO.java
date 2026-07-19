package com.LionKing.Teamply.domain.projects.dto.request;

import java.time.LocalDateTime;

public class ProjectsReqDTO {

    public record ProjectCreateReq(
            String name,
            String projectType,
            String title,
            LocalDateTime deadline
    ){

    }
}
