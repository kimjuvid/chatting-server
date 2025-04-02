package com.example.chatting.repository.entity;

import com.example.chatting.common.FriendsStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "friend")
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User requester;

    @ManyToOne
    private User accepter;

    @Enumerated(EnumType.STRING)
    private FriendsStatus friendsStatus;

    private LocalDateTime requestedAt;
    private LocalDateTime updatedAt;
}
