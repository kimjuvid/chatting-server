package com.example.chatting.model.friend.response;

import com.example.chatting.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "friend content")
public record FriendListResponse(
        @Schema(description = "성공 유무")
        ErrorCode description
) {
}
