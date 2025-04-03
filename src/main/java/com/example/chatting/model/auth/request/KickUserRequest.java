package com.example.chatting.model.auth.request;

public record KickUserRequest(
         Long roomId,
         String targetName
) {
}
