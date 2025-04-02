package com.example.chatting.model.friend.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "친구 요청 목록 응답")
public record FriendRequestItem(
        Long requestId,
        String requesterName
) {
}
