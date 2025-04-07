package com.example.chatting.model.auth.response;

import com.example.chatting.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "유저 생성 response")
public record CreateUserResponse(
        @Schema(description = "성공 유무")
        ErrorCode description
) {
}
