package com.example.chatting.model.multiChat.response;

import com.example.chatting.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Create ChatRoom", description = "Create ChatRoom")
public record CreateChatRoomResponse(
        @Schema(description = "성공 유무")
        ErrorCode description
) {
}
