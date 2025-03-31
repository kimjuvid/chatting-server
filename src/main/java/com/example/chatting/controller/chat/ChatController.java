package com.example.chatting.controller.chat;

import com.example.chatting.model.chat.Message;
import com.example.chatting.model.chat.response.ChatListResponse;
import com.example.chatting.service.chat.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Chat API", description = "V1 Chat API")
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @Operation(
            summary = "채팅 리스트를 가져옵니다.",
            description = "가장 최근 10개의 채팅 리스트를 가져옵니다."
    )
    @GetMapping("/chat-list")
    public ChatListResponse chatList(
            @RequestParam("name") String from,
            @RequestParam("to") String to
    ) {
        return chatService.chatList(from, to);
    }
}
