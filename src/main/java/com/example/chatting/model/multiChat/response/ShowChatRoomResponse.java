package com.example.chatting.model.multiChat.response;

import com.example.chatting.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "Show ChatRoom", description = "Show ChatRoom")
public record ShowChatRoomResponse(
        @Schema(description = "성공 유무")
        ErrorCode description,

        @Schema(description = "나의 채팅방 리스트")
        List<RoomInfo> roomInfos

) {
}
