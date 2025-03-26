package com.example.chatting.service.chat;

import com.example.chatting.model.chat.Message;
import com.example.chatting.repository.ChatRepository;
import com.example.chatting.repository.entity.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRepository chatRepository;

    public List<Message> chatList(String from, String to) {
        List<Chat> chats = chatRepository.findChatsBetweenUsers(from, to);

        List<Message> res = new ArrayList<>();
        for (Chat chat : chats) {
            Message msg = new Message(chat.getReceiver(), chat.getSender(), chat.getMessage());
            res.add(msg);
        }

        return res;
    }

    @Transactional
    public void saveChatMessage(Message msg) {
        Chat chat = Chat.builder().
                sender(msg.getFrom()).
                receiver(msg.getTo()).
                message(msg.getMessage()).
                created_at(new Timestamp(System.currentTimeMillis())).
                build();

        chatRepository.save(chat);
    }
}
