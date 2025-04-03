package com.example.chatting.controller.chat;

import com.example.chatting.model.chat.ChatMessage;
import com.example.chatting.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
@Slf4j
public class WssController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    // 대화 내용 저장
    @MessageMapping("/chat/message")
    public void sendMessage(ChatMessage message) {
        log.info("Received message in room {}: {}", message.getChatRoomId(), message.getMessage());
        chatService.saveChatMessage(message);

        // 해당 방을 구독 중인 사용자에게 메시지 전송
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getChatRoomId(), message);
    }
}