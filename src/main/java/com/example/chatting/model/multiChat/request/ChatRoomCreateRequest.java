package com.example.chatting.model.multiChat.request;

import com.example.chatting.common.ChatRoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "채팅방 생성 요청 DTO")
public record ChatRoomCreateRequest(
        @Schema(description = "채팅방 이름")
        @NotBlank
        String roomName,

        @Schema(description = "채팅방 생성자(OWNER)의 이름")
        @NotBlank
        String ownerName,

        @Schema(description = "참여자 이름 리스트")
        @NotEmpty
        List<String> userNames
) {
}
