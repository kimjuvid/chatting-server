package com.example.chatting.service.chat;

import com.example.chatting.common.exception.CustomException;
import com.example.chatting.common.exception.ErrorCode;
import com.example.chatting.model.chat.ChatMessage;
import com.example.chatting.repository.ChatRepository;
import com.example.chatting.repository.ChatRoomRepository;
import com.example.chatting.repository.UserRepository;
import com.example.chatting.repository.entity.Chat;
import com.example.chatting.repository.entity.ChatRoom;
import com.example.chatting.repository.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    // 대화 내용 저장
    public void saveChatMessage(ChatMessage message) {
        User sender = userRepository.findByName(message.getSender())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));

        ChatRoom room = chatRoomRepository.findById(message.getChatRoomId())
                .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_EXISTS));

        Chat chat = Chat.builder()
                .chatRoom(room)
                .sender(sender)
                .message(message.getMessage())
                .sentAt(LocalDateTime.now())
                .build();

        chatRepository.save(chat);
    }
}