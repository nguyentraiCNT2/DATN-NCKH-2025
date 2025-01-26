package org.ninhngoctuan.backend.config;

import org.ninhngoctuan.backend.dto.MessageDTO;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class CustomWebSocketHandler extends TextWebSocketHandler {

    private Set<WebSocketSession> sessions = new HashSet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Khi có một kết nối WebSocket mới được tạo, lưu session vào set
        sessions.add(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Khi nhận được tin nhắn từ client, gửi lại cho tất cả các session đã kết nối
        for (WebSocketSession webSocketSession : sessions) {
            if (webSocketSession.isOpen()) {
                webSocketSession.sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        // Khi một kết nối WebSocket đóng, loại bỏ session
        sessions.remove(session);
    }

    // Phương thức này sẽ phát tin nhắn đến tất cả các session đã kết nối
    public void broadcastMessage(MessageDTO message) {
        TextMessage textMessage = new TextMessage(message.getContent());
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(textMessage);
                } catch (IOException e) {
                    e.printStackTrace();  // Log lỗi nếu có
                }
            }
        }
    }
}
