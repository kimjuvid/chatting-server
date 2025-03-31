package com.example.chatting.repository.entity;

import com.example.chatting.common.ChatRoomType;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ChatRoomType chatRoomType;      // private, group ..

    private Integer maxParticipants;

    private LocalDateTime createAt;
    private LocalDateTime clodeAt;

}
