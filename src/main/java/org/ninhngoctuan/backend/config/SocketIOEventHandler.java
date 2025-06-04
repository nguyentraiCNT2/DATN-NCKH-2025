package org.ninhngoctuan.backend.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SocketIOEventHandler {

    @Autowired
    private SocketIOServer socketIOServer;

    @OnConnect
    public void onConnect(com.corundumstudio.socketio.SocketIOClient client) {
        // Lấy userId từ HandshakeData
        List<String> userIdList = client.getHandshakeData().getUrlParams().get("userId");
        String userId = (userIdList != null && !userIdList.isEmpty()) ? userIdList.get(0) : null;

        if (userId != null) {
            client.set("userId", userId); // Lưu userId vào client
            System.out.println("Client connected: " + client.getSessionId() + ", userId: " + userId);
        } else {
            System.out.println("Client connected: " + client.getSessionId() + ", no userId");
        }
    }

    @OnDisconnect
    public void onDisconnect(com.corundumstudio.socketio.SocketIOClient client) {
        System.out.println("Client disconnected: " + client.getSessionId());
    }

    @OnEvent("joinRoom")
    public void onJoinRoom(com.corundumstudio.socketio.SocketIOClient client, String roomId) {
        String userId = client.get("userId");
        client.joinRoom(roomId);
        System.out.println("Client " + client.getSessionId() + " (userId: " + (userId != null ? userId : "none") + ") joined room " + roomId);
    }

    @OnEvent("sendMessage")
    public void onSendMessage(com.corundumstudio.socketio.SocketIOClient client, MessageData messageData) {
        String userId = client.get("userId");
        socketIOServer.getRoomOperations(messageData.getRoomId()).sendEvent("newMessage", messageData.getMessage());
        System.out.println("New message from userId " + (userId != null ? userId : "none") + " in room " + messageData.getRoomId() + ": " + messageData.getMessage());
    }
}

class MessageData {
    private String roomId;
    private Object message;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}