package com.LionKing.Teamply.domain.project.controller;

import com.LionKing.Teamply.domain.project.dto.request.ProjectReqDTO;
import com.LionKing.Teamply.domain.project.dto.response.ProjectResDTO;
import com.LionKing.Teamply.domain.project.service.command.ProjectCommandService;
import com.LionKing.Teamply.domain.project.service.query.ProjectQueryService;
import com.LionKing.Teamply.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "프로젝트 api", description = "프로젝트 관련 api")
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProjectController {

    private final ProjectCommandService projectCommandService;
    private final ProjectQueryService projectQueryService;

    @PostMapping("/projects/create") // 오타(cerate -> create) 수정
    @Operation(summary = "프로젝트 생성", description = "내 프로젝트를 생성합니다.")
    public ApiResponse<ProjectResDTO.ProjectCreateRes> createProject(
            @AuthenticationPrincipal Long userId,
            @RequestBody ProjectReqDTO.ProjectCreateReq projectCreateReq
    ){
        ProjectResDTO.ProjectCreateRes projectCreateRes = projectCommandService.createProject(projectCreateReq, userId);
        return ApiResponse.success(201, "프로젝트 생성 성공", projectCreateRes);
    }

    @GetMapping("/projects/{projectId}/summary")
    @Operation(summary = "프로젝트 정보 조회", description = "내 프로젝트 관련 정보들을 조회합니다.")
    public ApiResponse<ProjectResDTO.ProjectSummaryRes> getProject(
            @PathVariable Long projectId
    ){
        ProjectResDTO.ProjectSummaryRes projectSummaryRes = projectQueryService.getProject(projectId);
        return ApiResponse.success("프로젝트 조회 성공", projectSummaryRes);
    }

    @GetMapping("/projects")
    @Operation(summary = "프로젝트 목록 조회", description = "내 프로젝트 목록들을 조회합니다.")
    public ApiResponse<List<ProjectResDTO.ProjectListRes>> getProjects(
            @AuthenticationPrincipal Long userId
    ) {
        List<ProjectResDTO.ProjectListRes> projects = projectQueryService.getProjects(userId);
        return ApiResponse.success("프로젝트 목록 조회 성공", projects);
    }
}
