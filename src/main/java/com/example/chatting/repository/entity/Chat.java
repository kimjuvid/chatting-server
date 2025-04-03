package com.example.chatting.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "t_id")
    private Long TID;

    @Column
    private String message;

    @ManyToOne
    private User sender;

    @ManyToOne
    private ChatRoom chatRoom;

    private LocalDateTime sentAt;
}
