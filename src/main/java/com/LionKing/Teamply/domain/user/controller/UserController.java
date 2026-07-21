package com.LionKing.Teamply.domain.user.controller;

import com.LionKing.Teamply.domain.user.dto.request.ChangePasswordRequest;
import com.LionKing.Teamply.domain.user.dto.response.UserResponse;
import com.LionKing.Teamply.domain.user.service.command.UserCommandService;
import com.LionKing.Teamply.domain.user.service.query.UserQueryService;
import com.LionKing.Teamply.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
public class UserController {

    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    @GetMapping
    public ApiResponse<UserResponse> getMyInfo(@AuthenticationPrincipal Long userId) {
        return ApiResponse.success("내 정보 조회 성공", userQueryService.getMyInfo(userId));
    }


    @PatchMapping("/password")
    public ApiResponse<Void> changePassword(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        userCommandService.changePassword(userId, request);
        return ApiResponse.success("비밀번호가 성공적으로 변경되었습니다.");
    }
}