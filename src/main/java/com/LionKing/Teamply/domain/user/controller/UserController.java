package com.LionKing.Teamply.domain.user.controller;

import com.LionKing.Teamply.domain.ai.dto.response.AiFeedbackResponse;
import com.LionKing.Teamply.domain.ai.service.query.AiFeedbackQueryService;
import com.LionKing.Teamply.domain.user.dto.request.ChangePasswordRequest;
import com.LionKing.Teamply.domain.user.dto.response.ActivityResponse;
import com.LionKing.Teamply.domain.user.dto.response.UserResponse;
import com.LionKing.Teamply.domain.user.service.command.UserCommandService;
import com.LionKing.Teamply.domain.user.service.query.UserActivityQueryService;
import com.LionKing.Teamply.domain.user.service.query.UserQueryService;
import com.LionKing.Teamply.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Auth", description = "회원가입 / 로그인 / 토큰 재발급 / 로그아웃 / 마이페이지(내 정보, 비밀번호 변경) API")
@io.swagger.v3.oas.annotations.responses.ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "생성 성공", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "USER404: 존재하지 않는 사용자 정보입니다.", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "USER400_1: 기존 비밀번호가 일치하지 않거나, 새 비밀번호 형식이 올바르지 않습니다.", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "USER400_2: 잘못된 페이징 파라미터입니다.", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class)))
})
@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
public class UserController {

    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;
    private final UserActivityQueryService userActivityQueryService;
    private final AiFeedbackQueryService aiFeedbackQueryService;

    @Operation(summary = "내 정보 조회 (마이페이지)", description = "로그인한 사용자의 프로필 정보를 조회합니다.")
    @GetMapping
    public ApiResponse<UserResponse> getMyInfo(@AuthenticationPrincipal Long userId) {
        return ApiResponse.success("내 정보 조회 성공", userQueryService.getMyInfo(userId));
    }

    @Operation(summary = "프로필 사진 변경", description = "마이페이지에서 내 프로필 사진을 업로드/변경합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(type = "object", requiredProperties = {"image"}),
                    encoding = @Encoding(name = "image", contentType = "image/png, image/jpeg")
            )
    )
    @PatchMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<UserResponse.ProfileImageUpdateRes> updateProfileImage(
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "업로드할 프로필 이미지 파일", required = true)
            @RequestPart("image") MultipartFile image
    ) {
        return ApiResponse.success("프로필 사진 변경 성공",
                userCommandService.updateProfileImage(userId, image));
    }

    @Operation(summary = "프로필 사진 삭제", description = "마이페이지에서 내 프로필 사진을 기본 이미지로 되돌립니다.")
    @DeleteMapping("/profile-image")
    public ApiResponse<Void> deleteProfileImage(
            @AuthenticationPrincipal Long userId
    ) {
        userCommandService.deleteProfileImage(userId);
        return ApiResponse.success("프로필 사진 삭제 성공");
    }

    @Operation(summary = "AI 피드백 기록 조회", description = "로그인한 사용자의 AI 피드백 이력을 페이지 단위로 조회합니다.")
    @GetMapping("/ai-feedbacks")
    public ApiResponse<AiFeedbackResponse.FeedbackPage> getMyAiFeedbacks(
            @AuthenticationPrincipal Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.success("AI 피드백 기록 조회 성공",
                aiFeedbackQueryService.getMyFeedbacks(userId, page, size));
    }
}