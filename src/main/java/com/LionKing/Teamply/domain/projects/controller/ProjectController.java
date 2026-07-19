package com.LionKing.Teamply.domain.projects.controller;

import com.LionKing.Teamply.domain.projects.dto.request.ProjectsReqDTO;
import com.LionKing.Teamply.domain.projects.dto.response.ProjectsResDTO;
import com.LionKing.Teamply.domain.projects.service.command.ProjectCommandService;
import com.LionKing.Teamply.domain.projects.service.query.ProjectQueryService;
import com.LionKing.Teamply.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "프로젝트 api", description = "프로젝트 관련 api")
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProjectController {

    private final ProjectCommandService projectCommandService;
    private final ProjectQueryService projectQueryService;

    @PostMapping("/projects/cerate")
    @Operation(summary = "프로젝트 생성", description = "내 프로젝트를 생성합니다.")
    public ApiResponse<ProjectsResDTO.ProjectCreateRes> createProject(
            @RequestBody ProjectsReqDTO.ProjectCreateReq ProjectCreateReq
    ){
        ProjectsResDTO.ProjectCreateRes projectCreateRes = projectCommandService.createProject(ProjectCreateReq);
        return ApiResponse
                .success(201, "프로젝트 생성 성공", projectCreateRes);
    }

    @GetMapping("/projects/{projectId}/summary")
    @Operation(summary = "프로젝트 정보 조회", description = "내 프로젝트 관련 정보들을 조회합니다.")
    public ApiResponse<ProjectsResDTO.ProjectGetRes> getProject(
            @PathVariable Long projectId
    ){
        ProjectsResDTO.ProjectGetRes projectGetRes = projectQueryService.getProject(projectId);
        return ApiResponse.success("프로젝트 조회 성공", projectGetRes);
    }

    @GetMapping("/projects")
    @Operation(summary = "프로젝트 목록 조회", description = "내 프로젝트 목록들을 조회합니다.")
    public ApiResponse<List<ProjectsResDTO.ProjectGetRes>> getProjects(
            @RequestParam String email
    ) {
        List<ProjectsResDTO.ProjectGetRes> projects = projectQueryService.getProjects(email);
        return ApiResponse.success("프로젝트 목록 조회 성공", projects);
    }
}
