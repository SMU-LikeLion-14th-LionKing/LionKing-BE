package com.LionKing.Teamply.domain.auth.controller;

import com.LionKing.Teamply.domain.auth.dto.request.LoginRequest;
import com.LionKing.Teamply.domain.auth.dto.request.ReissueRequest;
import com.LionKing.Teamply.domain.auth.dto.request.SignupRequest;
import com.LionKing.Teamply.domain.auth.dto.response.ReissueResponse;
import com.LionKing.Teamply.domain.auth.dto.response.SignupResponse;
import com.LionKing.Teamply.domain.auth.dto.response.TokenResponse;
import com.LionKing.Teamply.domain.auth.service.AuthService;
import com.LionKing.Teamply.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "회원가입 / 로그인 / 토큰 재발급 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "이름, 이메일, 비밀번호로 회원가입합니다.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> signup(@Valid @RequestBody SignupRequest request) {
        SignupResponse response = authService.signup(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "회원가입이 성공적으로 완료되었습니다.", response));
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인하여 Access/Refresh Token을 발급받습니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse tokenResponse = authService.login(request);
        return ResponseEntity.ok(
                ApiResponse.success(200, "로그인에 성공했습니다.", tokenResponse)
        );
    }

    @Operation(summary = "토큰 재발급", description = "Refresh Token으로 새로운 Access/Refresh Token을 발급받습니다.")
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<ReissueResponse>> reissue(@Valid @RequestBody ReissueRequest request) {

        TokenResponse token = authService.reissue(request.getRefreshToken());
        ReissueResponse reissueResponse = ReissueResponse.of(token.getAccessToken(), token.getRefreshToken());

        return ResponseEntity.ok(
                ApiResponse.success(200, "토큰 재발급에 성공하였습니다.", reissueResponse)
        );
    }
}