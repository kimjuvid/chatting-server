package com.example.chatting.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WssConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub");    // 서버 -> 클라이언트(브로드캐스트 채널, 구독 경로)
        registry.setApplicationDestinationPrefixes("/pub"); // 클라이언트 -> 서버
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry register ) {
        register.addEndpoint("/ws-stomp")   // 클라이언트가 이걸로 소켓 연결
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
