package com.example.chatting.controller.friend;

import com.example.chatting.common.FriendsStatus;
import com.example.chatting.model.friend.response.FriendListResponse;
import com.example.chatting.model.friend.response.FriendRequestListResponse;
import com.example.chatting.service.friend.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Friend API",description = "v1 Friend API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friend")
public class FriendController {

    private final FriendService friendService;

    @Operation(summary = "친구 요청을 진행합니다.",description = "친구 요청")
    @PostMapping("/request")
    public FriendListResponse friendRequest(@RequestParam("accepter") String accepter ){
        return friendService.friendRequest(accepter);
    }



    @Operation(summary = "내가 받은 친구 요청 목록", description = "수락 또는 거절 가능한 친구 요청 리스트를 반환합니다.")
    @GetMapping("/request/receivedlist")
    public FriendRequestListResponse getReceivedFriendRequests(){
        return friendService.getReceivedFriendRequests();
    }

    // 친구 요청을 수락/거절 하는 코드블록
    @Operation(summary = "내가 받은 친구 요청 수락 및 거절", description = "친구 요청 리스트를 확인하고 친구 요청을 수락 또는 거절을 합니다.")
    @PostMapping("/request/receivedlist/handle")
    public FriendListResponse handleFriendRequestAction(@RequestParam Long requestId,
                                                        @RequestParam FriendsStatus status){
        return friendService.handleFriendRequestAction(requestId, status);
    }


    // 현재 나랑 친구관계인 사람들 보여주는 코드블록
    @Operation(summary = "나의 친구 목록 확인", description = "나와 친구 관계가 성립된 친구 리스트를 확인합니다.")
    @GetMapping("/list")
    public FriendRequestListResponse getMyFriendList(){
        return friendService.getMyFriendList();
    }


    // 내 친구 검색하기
    @Operation(summary = "나의 친구 검색", description = "나와 친구 관계가 성립된 친구를 검색합니다.")
    @GetMapping("/list/search")
    public FriendRequestListResponse searchFriend(@RequestParam String keyword) {
        return friendService.searchFriend(keyword);
    }

    @Operation(summary = "나의 친구 삭제", description = "나와 친구 관계가 성립된 친구를 선택하여 친구 삭제를 진행합니다.")
    @DeleteMapping("/list/delete")
    public FriendListResponse deleteMyFriend(@RequestParam Long requestId){
        return friendService.deleteMyFriend(requestId);
    }
}
