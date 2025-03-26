package com.example.chatting.repository;

import com.example.chatting.repository.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat,Long> {
    //List<Chat> findTop10BySenderOrReceiverOrderByTIDDesc(String sender, String Receiver);

    @Query("SELECT c FROM Chat c " +
            "WHERE (c.sender = :sender AND c.receiver = :receiver) " +
            "   OR (c.sender = :receiver AND c.receiver = :sender) " +
            "ORDER BY c.TID DESC")
    List<Chat> findChatsBetweenUsers(@Param("sender") String from,
                                     @Param("receiver") String to);
}
