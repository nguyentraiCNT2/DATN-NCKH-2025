package org.ninhngoctuan.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // Đăng ký WebSocket handler cho endpoint
        registry.addHandler(new CustomWebSocketHandler(), "/ws")
                .addInterceptors(new HttpSessionHandshakeInterceptor())  // Thêm interceptor nếu cần thiết
                .setAllowedOrigins("*"); // Cho phép tất cả các nguồn kết nối
    }

    // Đăng ký CustomWebSocketHandler như một Spring Bean
    @Bean
    public CustomWebSocketHandler customWebSocketHandler() {
        return new CustomWebSocketHandler();
    }
}
