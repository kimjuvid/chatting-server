package com.example.chatting.repository;

import com.example.chatting.repository.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat,Long> {

    List<Chat> findAllByChatRoomIdOrderBySentAtAsc(Long chatRoomId);
}
