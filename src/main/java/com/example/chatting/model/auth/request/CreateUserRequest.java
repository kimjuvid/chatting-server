package com.example.chatting.model.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "User를 생성합니다.")
public record CreateUserRequest(
        @Schema(description = "유저명")
        @NotNull
        @NotBlank
        String name,
        @Schema(description = "유저 비번")
        @NotNull
        @NotBlank
        String password) {
}
