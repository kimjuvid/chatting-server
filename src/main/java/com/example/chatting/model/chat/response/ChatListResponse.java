package com.example.chatting.model.chat.response;

import com.example.chatting.model.multiChat.ChatMessageResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Chatting content")
public record ChatListResponse(
        @Schema(description = "return message")
        List<ChatMessageResponse> result
) {
}
