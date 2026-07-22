package com.LionKing.Teamply.domain.post.controller;

import com.LionKing.Teamply.domain.post.dto.request.PostReqDTO;
import com.LionKing.Teamply.domain.post.dto.response.PostResDTO;
import com.LionKing.Teamply.domain.post.service.command.PostCommandService;
import com.LionKing.Teamply.domain.post.service.query.PostQueryService;
import com.LionKing.Teamply.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Tag(name = "게시글 관련 API", description = "게시글(작업,질문,회의록,공지사항)관련 API")
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;

    @PostMapping("projects/{projectId}/notice")
    @Operation(summary = "공지사항 생성", description = "공지사항을 작성합니다.")
    public ApiResponse<PostResDTO.NoticeCreateRes> createNotice(
            @PathVariable Long projectId,
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody PostReqDTO.NoticeCreateReq req
    ) {
        PostResDTO.NoticeCreateRes res = postCommandService.createNotice(projectId, userId, req);
        return ApiResponse.success(201, "공지사항이 생성되었습니다.", res);
    }

    @GetMapping("projects/{projectId}/notice")
    @Operation(summary = "공지사항 목록 조회", description = "프로젝트의 공지사항들을 N개씩 가져옵니다.")
    public ApiResponse<Page<PostResDTO.NoticesGetRes>> getNotices(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<PostResDTO.NoticesGetRes> res = postQueryService.getNotices(projectId, page, size);
        return ApiResponse.success("공지사항 목록 조회 성공", res);
    }

    @GetMapping("projects/{projectId}/notice/recent")
    @Operation(summary = "공지사항 목록 요약 조회", description = "최근 공지사항중 3개를 요약해서 가져옵니다.")
    public ApiResponse<List<PostResDTO.RecentNoticeRes>> getRecentNotices(
            @PathVariable Long projectId
    ) {
        List<PostResDTO.RecentNoticeRes> res = postQueryService.getRecentNotices(projectId);
        return ApiResponse.success("최근 공지사항 조회 성공", res);
    }

    /*--작업게시글--*/

    @PostMapping("/projects/{projectId}/posts/task")
    @Operation(summary = "작업 게시글 작성")
    public ApiResponse<PostResDTO.PostCreateRes> createTask(
            @PathVariable Long projectId,
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody PostReqDTO.TaskCreateReq req
    ) {
        return ApiResponse.success(201, "작업 게시글이 성공적으로 등록되었습니다.",
                postCommandService.createTask(projectId, userId, req));
    }

    @PatchMapping("/posts/{postId}/task")
    @Operation(summary = "작업 게시글 수정")
    public ApiResponse<PostResDTO.PostUpdateRes> updateTask(
            @PathVariable Long postId,
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody PostReqDTO.PostUpdateReq req
    ) {
        return ApiResponse.success(200, "작업 게시글이 수정되었습니다.",
                postCommandService.updateTask(postId, userId, req));
    }

    /*--질문게시글--*/
    @PostMapping("/projects/{projectId}/posts/question")
    @Operation(summary = "질문 게시글 작성")
    public ApiResponse<PostResDTO.PostCreateRes> createQuestion(
            @PathVariable Long projectId,
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody PostReqDTO.QuestionCreateReq req
    ) {
        return ApiResponse.success(201, "질문 게시글이 성공적으로 등록되었습니다.",
                postCommandService.createQuestion(projectId, userId, req));
    }

    @PatchMapping("/posts/{postId}/question")
    @Operation(summary = "질문 게시글 수정")
    public ApiResponse<PostResDTO.PostUpdateRes> updateQuestion(
            @PathVariable Long postId,
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody PostReqDTO.PostUpdateReq req
    ) {
        return ApiResponse.success(200, "질문 게시글이 수정되었습니다.",
                postCommandService.updateQuestion(postId, userId, req));
    }

    /*--게시글 목록/상세/삭제--*/
    @GetMapping("/projects/{projectId}/posts")
    @Operation(summary = "게시글 목록 조회", description = "카테고리별로 게시글을 페이징 조회합니다.")
    public ApiResponse<Page<PostResDTO.PostListItemRes>> getPosts(
            @PathVariable Long projectId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.success("게시글 목록 조회 성공",
                postQueryService.getPosts(projectId, categoryId, page, size));
    }

    @GetMapping("/posts/{postId}")
    @Operation(summary = "게시글 상세 조회")
    public ApiResponse<PostResDTO.PostDetailRes> getPostDetail(
            @PathVariable Long postId
    ) {
        return ApiResponse.success("게시글 상세 조회 성공",
                postQueryService.getPostDetail(postId));
    }

    @DeleteMapping("/posts/{postId}")
    @Operation(summary = "게시글 삭제")
    public ApiResponse<Void> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal Long userId
    ) {
        postCommandService.deletePost(postId, userId);
        return ApiResponse.success("게시글이 삭제되었습니다.");
    }

    /*--첨부파일--*/
    @PostMapping(value = "/projects/{projectId}/files", consumes = "multipart/form-data")
    @Operation(summary = "첨부파일 업로드")
    public ApiResponse<PostResDTO.FileUploadRes> uploadFile(
            @PathVariable Long projectId,
            @AuthenticationPrincipal Long userId,
            @RequestPart("file") MultipartFile file
    ) {
        return ApiResponse.success(201, "파일이 성공적으로 업로드되었습니다.",
                postCommandService.uploadFile(projectId, userId, file));
    }

    @DeleteMapping("/files/{fileId}")
    @Operation(summary = "첨부파일 삭제")
    public ApiResponse<Void> deleteFile(
            @PathVariable Long fileId,
            @AuthenticationPrincipal Long userId
    ) {
        postCommandService.deleteFile(fileId, userId);
        return ApiResponse.success("파일이 삭제되었습니다.");
    }

}
