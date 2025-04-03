package com.example.chatting.model.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    private Long chatRoomId;
    private String sender;
    private String message;
    private String time;
}
