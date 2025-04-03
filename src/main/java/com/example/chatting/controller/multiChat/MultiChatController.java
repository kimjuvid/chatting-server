package com.example.chatting.controller.multiChat;

import com.example.chatting.model.auth.request.KickUserRequest;
import com.example.chatting.model.multiChat.ChatMessageResponse;
import com.example.chatting.model.multiChat.request.ChatRoomCreateRequest;
import com.example.chatting.model.multiChat.response.ChatRoomInfoResponse;
import com.example.chatting.model.multiChat.response.CreateChatRoomResponse;
import com.example.chatting.model.multiChat.response.ShowChatRoomResponse;
import com.example.chatting.repository.entity.ChatRoom;
import com.example.chatting.service.multiChat.ChatRoomService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "MultiChat API", description = "v1 MultiChat API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatroom")
public class MultiChatController {

    private final ChatRoomService chatRoomService;


    // 채팅방 대화 내용 불러오기
    @GetMapping("/{chatRoomId}/messages")
    public List<ChatMessageResponse> getMessages(@PathVariable Long chatRoomId) {
        return chatRoomService.getChatMessages(chatRoomId);
    }

    // 채팅방 생성
    @PostMapping("/create")
    public CreateChatRoomResponse createChatRoom(@RequestBody ChatRoomCreateRequest request){
        return chatRoomService.createChatRoom(request);
    }

    // 내가 참여한 채팅방 목록 확인
    @GetMapping("/show")
    public ShowChatRoomResponse showChatRoom(){
        return chatRoomService.showChatRoom();
    }

    @GetMapping("/{chatRoomId}/info")
    public ChatRoomInfoResponse getChatRoomInfo(@PathVariable Long chatRoomId) {
        return chatRoomService.getChatRoomInfo(chatRoomId);
    }


    // 방장 권한으로 강퇴 설정
    @PostMapping("/kick")
    public ResponseEntity<String> kickUser(@RequestBody KickUserRequest request) {
        String requester = SecurityContextHolder.getContext().getAuthentication().getName();
        chatRoomService.kickUser(request.roomId(), requester, request.targetName());
        return ResponseEntity.ok("강퇴 완료");
    }

    // 채팅방 나가기
    @DeleteMapping("/leave")
    public void leaveChatRoom(@RequestParam Long roomId){
        chatRoomService.leaveChatRoom(roomId);
    }

}
