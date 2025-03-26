package com.example.chatting.controller.chat;

import com.example.chatting.model.chat.Message;
import com.example.chatting.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
@Slf4j
public class WssController {

    private final ChatService chatService;

    @MessageMapping("/chat/message/{from}")
    @SendTo("/sub/chat")
    public Message receivedMessage(
            @DestinationVariable String from,
            Message msg
    ) {
        log.info("Message Received -> From: {}, to: {}, msg: {}", from , msg.getTo(), msg.getFrom());
        chatService.saveChatMessage(msg);
        return msg;
    }
}
