package com.example.chatting.repository.entity;

import com.example.chatting.common.ChatRole;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class ChatRoomMember {// 중간 테이블 역할
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ChatRoom chatRoom;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private ChatRole role;  // OWNER, MEMBER

    private LocalDateTime joinedAt;
}
