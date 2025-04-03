package com.example.chatting.service.friend;

import com.example.chatting.common.FriendsStatus;
import com.example.chatting.common.exception.CustomException;
import com.example.chatting.common.exception.ErrorCode;
import com.example.chatting.model.friend.response.FriendListResponse;
import com.example.chatting.model.friend.response.FriendRequestItem;
import com.example.chatting.model.friend.response.FriendRequestListResponse;
import com.example.chatting.repository.FriendRepository;
import com.example.chatting.repository.UserRepository;
import com.example.chatting.repository.entity.Friend;
import com.example.chatting.repository.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    // 친구 요청
    public FriendListResponse friendRequest(String accepterName){

        // 요청자 정보
        String requesterName = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> requester = userRepository.findByName(requesterName);

        // 수락자 정보
        Optional<User> accepter = userRepository.findByName(accepterName);
        if (!accepter.isPresent() || !requester.isPresent()){
            throw new CustomException(ErrorCode.USER_NOT_EXISTS);
        }

        // 이미 친구 요청이 존재하는 상태인지 확인
        if (friendRepository.existsByRequesterAndAccepter(requester.get(), accepter.get())){
            throw new CustomException(ErrorCode.FRIEND_REQUEST_ALREADY_EXISTS);
        }

        Friend friend = Friend.builder()
                .requester(requester.get())
                .accepter(accepter.get())
                .friendsStatus(FriendsStatus.REQUESTED)
                .requestedAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        friendRepository.save(friend);

        return new FriendListResponse(ErrorCode.SUCCESS);
    }

    // 요청받은 친구 목록 확인
    public FriendRequestListResponse getReceivedFriendRequests(){

        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByName(currentUserName)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_EXISTS));

        List<Friend> requestList = friendRepository.findAllByAccepterAndFriendsStatus(currentUser, FriendsStatus.REQUESTED);

        List<FriendRequestItem> items = requestList.stream().map(f -> new FriendRequestItem(f.getId(), f.getRequester().getName())).toList();
        return new FriendRequestListResponse(items);
    }

    // 친구 수락 및 거절
    public FriendListResponse handleFriendRequestAction(Long requestId, FriendsStatus status){

        // 1. 친구 요청 엔티티 조회
        Friend friendRequest = friendRepository.findById(requestId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        // 2. 현재 로그인 사용자와 수락 대상 일치 여부 확인
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!friendRequest.getAccepter().getName().equals(currentUserName)) {
            throw new CustomException(ErrorCode.USER_CHECKING_FAIL);
        }

        // 3. 현재 상태가 REQUESTED 인지 확인
        if (!friendRequest.getFriendsStatus().equals(FriendsStatus.REQUESTED)) {
            throw new CustomException(ErrorCode.INVALID_FRIEND_STATUS);
        }

        // 4. 상태가 ACCEPTED 또는 REJECTED인지 확인 후 반영
        if (status != FriendsStatus.ACCEPTED && status != FriendsStatus.REJECTED) {
            throw new CustomException(ErrorCode.INVALID_ACTION);
        }

        friendRequest.setFriendsStatus(status);
        friendRequest.setUpdatedAt(LocalDateTime.now());
        friendRepository.save(friendRequest);

        // 5. 성공 응답
        return new FriendListResponse(ErrorCode.SUCCESS);
    }

    // 나의 친구 목록 확인하기
    public FriendRequestListResponse getMyFriendList(){

        String CurrentUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByName(CurrentUserName).orElseThrow(()->
                new CustomException(ErrorCode.NOT_EXIST_USER));

        List<Friend> friendList =
        friendRepository.findAllAcceptedFriendsByUser(FriendsStatus.ACCEPTED, user);

        List<FriendRequestItem> friendRequestItem = friendList.stream()
                .map(f -> {
                    User friendUser = f.getRequester().getName().equals(CurrentUserName)
                            ? f.getAccepter()
                            : f.getRequester();
                    return new FriendRequestItem(f.getId(), friendUser.getName()); // ✅ return 추가
                })
                .toList();
        return new FriendRequestListResponse(friendRequestItem);
    }

    // 나의 친구 검색하기
    public FriendRequestListResponse searchFriend(String keyword){

        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByName(currentUserName)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));

        List<Friend> friends = friendRepository.findAllAcceptedFriendsByUser(FriendsStatus.ACCEPTED, user);

        List<FriendRequestItem> filtered = friends.stream()
                .map(f -> {
                    User friendUser = f.getRequester().getName().equals(currentUserName)
                            ? f.getAccepter()
                            : f.getRequester();
                    return new FriendRequestItem(f.getId(), friendUser.getName());
                })
                .filter(item -> item.requesterName().contains(keyword))
                .toList();

        return new FriendRequestListResponse(filtered);
    }

    // 나의 친구 삭제하기
    public FriendListResponse deleteMyFriend(Long requestId){

        Friend friend = friendRepository.findById(requestId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));


        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!friend.getRequester().getName().equals(currentUser) &&
                !friend.getAccepter().getName().equals(currentUser)) {
            throw new CustomException(ErrorCode.USER_NOT_EXISTS);
        }

        friendRepository.delete(friend);
        return new FriendListResponse(ErrorCode.SUCCESS);
    }
}
