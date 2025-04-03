package com.example.chatting.model.multiChat;

import java.time.LocalDateTime;

public record ChatMessageResponse(
        String sender,
        String message,
        LocalDateTime createdAt
) {}
