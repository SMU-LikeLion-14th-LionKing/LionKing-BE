package com.LionKing.Teamply.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReissueRequest {

    @NotBlank(message = "Refresh Token은 필수입니다.")
    private String refreshToken;
}