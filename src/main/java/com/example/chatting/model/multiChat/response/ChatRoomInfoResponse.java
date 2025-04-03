package com.example.chatting.model.multiChat.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "ChatRoom Info", description = "ChatRoom Info")
public record ChatRoomInfoResponse(
        String roomName,
        List<String> participants,
        String ownerName
) {
}
