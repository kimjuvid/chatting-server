package com.example.chatting.model.auth.response;

import com.example.chatting.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 response")
public record LoginResponse(
        @Schema(description = "error code")
        ErrorCode description,

        @Schema(description = "jwt token")
        String token
) {
}
