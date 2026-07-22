package com.LionKing.Teamply.domain.user.controller;

import com.LionKing.Teamply.domain.user.dto.request.ChangePasswordRequest;
import com.LionKing.Teamply.domain.user.dto.response.UserResponse;
import com.LionKing.Teamply.domain.user.service.command.UserCommandService;
import com.LionKing.Teamply.domain.user.service.query.UserQueryService;
import com.LionKing.Teamply.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "회원가입 / 로그인 / 토큰 재발급 / 로그아웃 / 마이페이지(내 정보, 비밀번호 변경) API")
@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
public class UserController {

    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    @Operation(summary = "내 정보 조회 (마이페이지)", description = "로그인한 사용자의 프로필 정보를 조회합니다.")
    @GetMapping
    public ApiResponse<UserResponse> getMyInfo(@AuthenticationPrincipal Long userId) {
        return ApiResponse.success("내 정보 조회 성공", userQueryService.getMyInfo(userId));
    }

    @Operation(summary = "비밀번호 변경 (로그인 후)", description = "현재 비밀번호를 확인한 뒤 새로운 비밀번호로 변경합니다.")
    @PatchMapping("/password")
    public ApiResponse<Void> changePassword(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        userCommandService.changePassword(userId, request);
        return ApiResponse.success("비밀번호가 성공적으로 변경되었습니다.");
    }
}