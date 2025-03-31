package com.example.chatting.repository.entity;

import com.example.chatting.common.Role;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long t_id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;  // ROLE_USER, ROLE_ADMIN

    @Column
    private Timestamp created_at;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserCredentials userCredentials;

}
