package com.example.chatting.repository;

import com.example.chatting.common.FriendsStatus;
import com.example.chatting.repository.entity.Friend;
import com.example.chatting.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    // 이미 친구상태이거나 요청 상태인 경우인지 확인
    boolean existsByRequesterAndAccepter(User requester, User accepter);

    // 요청 받은 친구리스트
    List<Friend> findAllByAccepterAndFriendsStatus(User accepter, FriendsStatus friendsStatus);


    @Query("SELECT f FROM Friend f " +
            "WHERE f.friendsStatus = :status " +
            "AND (f.requester = :user OR f.accepter = :user)")
    List<Friend> findAllAcceptedFriendsByUser(@Param("status") FriendsStatus status, @Param("user") User user);

}

