package org.ninhngoctuan.backend.config;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class WebSocketConfig {

    @Value("${jwt.key}")
    private String SECRET_KEY;

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname("localhost");
        config.setPort(8081);

        // Cấu hình CORS
        config.setOrigin("http://localhost:3000");
        config.setAllowCustomRequests(true);

        // Xác thực JWT
        config.setAuthorizationListener(new AuthorizationListener() {
            @Override
            public boolean isAuthorized(HandshakeData data) {
                try {
                    // Log headers và query params để debug
                    System.out.println("Query params: " + data.getUrlParams());
                    System.out.println("Headers: " + data.getHttpHeaders());

                    // Lấy token từ query parameter
                    String token = data.getSingleUrlParam("token");
                    System.out.println("Token from query: " + token);

                    // Lấy token từ header Authorization
                    if (token == null || token.isEmpty()) {
                        String authHeader = data.getHttpHeaders().get("Authorization");
                        System.out.println("Authorization header: " + authHeader);
                        if (authHeader != null && authHeader.startsWith("Bearer ")) {
                            token = authHeader.substring(7);
                            System.out.println("Token from header: " + token);
                        }
                    }

                    if (token == null || token.isEmpty()) {
                        System.out.println("No token provided");
                        return false;
                    }

                    // Xác thực token
                    Claims claims = Jwts.parser()
                            .setSigningKey(SECRET_KEY)
                            .build()
                            .parseClaimsJws(token)
                            .getBody();

                    // Kiểm tra token có hết hạn không
                    if (claims.getExpiration().before(new Date())) {
                        System.out.println("Token expired");
                        return false;
                    }

                    // Lưu userId vào urlParams
                    data.getUrlParams().put("userId", java.util.Collections.singletonList(claims.getSubject()));
                    System.out.println("Token valid for user: " + claims.getSubject());
                    return true;
                } catch (Exception e) {
                    System.out.println("Token validation error: " + e.getMessage());
                    return false;
                }
            }
        });

        SocketIOServer server = new SocketIOServer(config);
        server.start();
        return server;
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketIOServer) {
        return new SpringAnnotationScanner(socketIOServer);
    }
}