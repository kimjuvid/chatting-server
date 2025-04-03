package com.example.chatting.service.multiChat;

import com.example.chatting.common.ChatRole;
import com.example.chatting.common.exception.CustomException;
import com.example.chatting.common.exception.ErrorCode;
import com.example.chatting.model.multiChat.ChatMessageResponse;
import com.example.chatting.model.multiChat.request.ChatRoomCreateRequest;
import com.example.chatting.model.multiChat.response.ChatRoomInfoResponse;
import com.example.chatting.model.multiChat.response.CreateChatRoomResponse;
import com.example.chatting.model.multiChat.response.RoomInfo;
import com.example.chatting.model.multiChat.response.ShowChatRoomResponse;
import com.example.chatting.repository.ChatRepository;
import com.example.chatting.repository.ChatRoomMemberRepository;
import com.example.chatting.repository.ChatRoomRepository;
import com.example.chatting.repository.UserRepository;
import com.example.chatting.repository.entity.Chat;
import com.example.chatting.repository.entity.ChatRoom;
import com.example.chatting.repository.entity.ChatRoomMember;
import com.example.chatting.repository.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;


    @Transactional
    public CreateChatRoomResponse createChatRoom(ChatRoomCreateRequest request) {

        ChatRoom chatRoom = ChatRoom.builder()
                .name(request.roomName())
                .createAt(LocalDateTime.now())
                .build();

        ChatRoom savedRoom = chatRoomRepository.save(chatRoom);

        for (String username : request.userNames()) {
            User user = userRepository.findByName(username)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));

            ChatRole role = username.equals(request.ownerName()) ? ChatRole.OWNER : ChatRole.MEMBER;

            ChatRoomMember member = ChatRoomMember.builder()
                    .chatRoom(savedRoom)
                    .user(user)
                    .role(role)
                    .joinedAt(LocalDateTime.now())
                    .build();

            chatRoomMemberRepository.save(member);
        }

        return new CreateChatRoomResponse(ErrorCode.SUCCESS);
    }


    @Transactional
    public ShowChatRoomResponse showChatRoom(){

        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        // 나의 기본키(id)를 받아와 User 엔티티를 채움
        User user = userRepository.findByName(currentUserName).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_EXISTS));

        List<ChatRoomMember> members = chatRoomMemberRepository.findAllByUser(user);
        List<RoomInfo> roomInfos = members.stream()
                .map(m -> new RoomInfo(m.getChatRoom().getId(), m.getChatRoom().getName())).toList();

        return new ShowChatRoomResponse(ErrorCode.SUCCESS, roomInfos);
    }

    @Transactional
    public void leaveChatRoom(Long roomId) {

        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByName(currentUserName)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_EXISTS));

        chatRoomMemberRepository.deleteByChatRoomAndUser(chatRoom, user);

        // 채팅방에 남은 인원이 0명이면 채팅방 종료 시간 입력 처리
        long remaining = chatRoomMemberRepository.countByChatRoom(chatRoom);
        if (remaining == 0) {
            chatRoom.setCloseAt(LocalDateTime.now());
            chatRoomRepository.save(chatRoom);
        }
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getChatMessages(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_EXISTS));

        List<Chat> chats = chatRepository.findAllByChatRoomIdOrderBySentAtAsc(chatRoomId);

        return chats.stream()
                .map(chat -> new ChatMessageResponse(chat.getSender().getName(), chat.getMessage(), chat.getSentAt()))
                .collect(Collectors.toList());
    }

    public ChatRoomInfoResponse getChatRoomInfo(Long chatRoomId){

        ChatRoom room = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_EXISTS));

        List<ChatRoomMember> members = chatRoomMemberRepository.findAllByChatRoom(room);

        List<String> participants = members.stream()
                .map(m -> m.getUser().getName())
                .toList();

        // ========= 재확인==========
        String ownerName = members.stream()
                .filter(m -> m.getRole() == ChatRole.OWNER)
                .map(m -> m.getUser().getName())
                .findFirst()
                .orElse("방장없음");

        return new ChatRoomInfoResponse(room.getName(), participants, ownerName);
    }



    // ==============
    @Transactional
    public void kickUser(Long roomId, String ownerName, String targetName) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_EXISTS));

        User owner = userRepository.findByName(ownerName)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));

        User target = userRepository.findByName(targetName)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));

        // 방장이 맞는지 확인 ===========
        ChatRoomMember ownerMember = chatRoomMemberRepository.findByChatRoomAndUser(chatRoom, owner)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_CHATROOM_MEMBER));

        if (ownerMember.getRole() != ChatRole.OWNER) {
            throw new CustomException(ErrorCode.NOT_ROOM_OWNER);
        }

        // 타겟 멤버가 실제 방에 있는지 확인 후 삭제
        chatRoomMemberRepository.deleteByChatRoomAndUser(chatRoom, target);
    }
}
