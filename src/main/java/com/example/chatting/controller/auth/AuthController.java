package com.example.chatting.controller.auth;

import com.example.chatting.model.auth.request.CreateUserRequest;
import com.example.chatting.model.auth.request.LoginRequest;
import com.example.chatting.model.auth.response.CreateUserResponse;
import com.example.chatting.model.auth.response.LoginResponse;
import com.example.chatting.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Auth API", description = "v1 Auth API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "새로운 유저를 생성합니다.",
            description = "새로운 유정 생성"
    )
    @PostMapping("/create-user")
    public CreateUserResponse createUser(@RequestBody @Valid CreateUserRequest request){
        return authService.createUser(request);
    }

    @Operation(
            summary = "로그인 처리",
            description = "로그인 진행"
    )
    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request){
        return authService.login(request);
    }
    
    // 서버 수평확장 test
    @GetMapping("/ping")
    public Map<String, String> ping(HttpServletRequest request) {
        String podName = System.getenv("HOSTNAME"); // Kubernetes에서 pod name으로 자동 주입됨
        return Map.of("message", "pong", "pod", podName);
    }
}
