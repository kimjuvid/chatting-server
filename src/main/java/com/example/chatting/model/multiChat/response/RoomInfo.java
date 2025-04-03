package com.example.chatting.model.multiChat.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Show ChatRoom", description = "Show ChatRoom")
public record RoomInfo(
        @Schema(description = "나의 채팅방 아이디")
        Long chatRoomId,

        @Schema(description = "나의 채팅방 리스트")
        String roomName

) {
}
