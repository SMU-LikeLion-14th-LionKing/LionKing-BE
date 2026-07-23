package com.LionKing.Teamply.domain.auth.controller;

import com.LionKing.Teamply.domain.auth.dto.request.LoginRequest;
import com.LionKing.Teamply.domain.auth.dto.request.PasswordResetRequest;
import com.LionKing.Teamply.domain.auth.dto.request.ReissueRequest;
import com.LionKing.Teamply.domain.auth.dto.request.SignupRequest;
import com.LionKing.Teamply.domain.auth.dto.response.ReissueResponse;
import com.LionKing.Teamply.domain.auth.dto.response.SignupResponse;
import com.LionKing.Teamply.domain.auth.dto.response.TokenResponse;
import com.LionKing.Teamply.domain.auth.service.command.AuthCommandService;
import com.LionKing.Teamply.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "회원가입 / 로그인 / 토큰 재발급 / 로그아웃 / 마이페이지(내 정보, 비밀번호 변경) API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthCommandService authCommandService;

    @Operation(summary = "회원가입", description = "이름, 이메일, 비밀번호로 회원가입합니다.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> signup(@Valid @RequestBody SignupRequest request) {
        SignupResponse response = authCommandService.signup(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "회원가입이 성공적으로 완료되었습니다.", response));
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인하여 Access/Refresh Token을 발급받습니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse tokenResponse = authCommandService.login(request);
        return ResponseEntity.ok(
                ApiResponse.success(200, "로그인에 성공했습니다.", tokenResponse)
        );
    }

    @Operation(summary = "토큰 재발급", description = "Refresh Token으로 새로운 Access/Refresh Token을 발급받습니다.")
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<ReissueResponse>> reissue(@Valid @RequestBody ReissueRequest request) {

        TokenResponse token = authCommandService.reissue(request.getRefreshToken());
        ReissueResponse reissueResponse = ReissueResponse.of(token.getAccessToken(), token.getRefreshToken());

        return ResponseEntity.ok(
                ApiResponse.success(200, "토큰 재발급에 성공하였습니다.", reissueResponse)
        );
    }

    @Operation(summary = "로그아웃", description = "Redis에 저장된 Refresh Token을 제거해 재발급을 막습니다.")
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@AuthenticationPrincipal Long userId) {
        authCommandService.logout(userId);
        return ApiResponse.success("성공적으로 로그아웃 되었습니다.");
    }

    @Operation(summary = "비밀번호 재설정 (로그인 전)", description = "이메일과 인증 코드로 본인 확인 후 비밀번호를 재설정합니다.")
    @PostMapping("/me/password")
    public ApiResponse<Void> resetPasswordBeforeLogin(@Valid @RequestBody PasswordResetRequest request) {
        authCommandService.resetPasswordBeforeLogin(request);
        return ApiResponse.success("비밀번호가 성공적으로 재설정되었습니다.");
    }
}