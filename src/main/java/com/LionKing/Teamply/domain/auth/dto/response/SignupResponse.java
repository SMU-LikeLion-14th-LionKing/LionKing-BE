
package com.LionKing.Teamply.domain.auth.dto.response;

import com.LionKing.Teamply.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonPropertyOrder({"user_id", "name", "email", "created_at"})
public class SignupResponse {

    @JsonProperty("user_id")
    private Long userId;

    private String name;

    private String email;

    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private LocalDateTime createdAt;

    public static SignupResponse from(User user) {
        return SignupResponse.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }
}