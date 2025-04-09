package com.example.chatting.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatPageTest {

    @GetMapping("/chat")
    public String chatPage(){
        return "chat.html";
    }
}
