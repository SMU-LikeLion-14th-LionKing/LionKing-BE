package com.LionKing.Teamply.domain.project.dto.request;

import java.time.LocalDateTime;

public class ProjectReqDTO {

    public record ProjectCreateReq(
            String name,
            String projectType,
            String title,
            LocalDateTime deadline
    ){
    }
}
