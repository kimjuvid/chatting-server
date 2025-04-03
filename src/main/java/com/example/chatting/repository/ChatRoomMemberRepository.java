package com.example.chatting.repository;

import com.example.chatting.repository.entity.ChatRoom;
import com.example.chatting.repository.entity.ChatRoomMember;
import com.example.chatting.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {

    Optional<ChatRoomMember> findByChatRoomAndUser(ChatRoom chatRoom, User user);

    List<ChatRoomMember> findAllByUser(User user);

    List<ChatRoomMember> findAllByChatRoom(ChatRoom chatRoom);

    void deleteByChatRoomAndUser(ChatRoom chatRoom, User user);

    // 채팅방에 남은 인원 수 확인
    long countByChatRoom(ChatRoom chatRoom);


}
