package com.example.chatting.model.friend.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "친구 목록 응답")
public record FriendRequestListResponse(

        @Schema(description = "친구 목록")
        List<FriendRequestItem> receivedFriendList
) {
}
