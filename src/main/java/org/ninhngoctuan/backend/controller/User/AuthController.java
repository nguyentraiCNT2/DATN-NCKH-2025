package org.ninhngoctuan.backend.controller.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ninhngoctuan.backend.Requests.RegisterRequest;
import org.ninhngoctuan.backend.context.RequestContext;
import org.ninhngoctuan.backend.controller.output.AuthStatus;
import org.ninhngoctuan.backend.dto.EmailActiveDTO;
import org.ninhngoctuan.backend.dto.PasswordResetDTO;
import org.ninhngoctuan.backend.dto.UserDTO;
import org.ninhngoctuan.backend.securityConfig.CustomUserDetailsService;
import org.ninhngoctuan.backend.securityConfig.JwtTokenUtil;
import org.ninhngoctuan.backend.service.LoginSessionService;
import org.ninhngoctuan.backend.service.TokenService;
import org.ninhngoctuan.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private LoginSessionService loginSessionService;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest dto){
        try {
            UserDTO userDTO =  userService.register(dto);
            userDTO.setPassword(null);
            RequestContext context = RequestContext.get();
            Map<String, Object> response = new HashMap<>();
            if (context != null) {
                response.put("requestId", context.getRequestId());
                response.put("userId", context.getUserId());
                response.put("timestamp", context.getTimestamp());
                response.put("userDetail",userDTO);
            }
            return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/active/email")
    public ResponseEntity<?> activeUserEmail(@RequestBody EmailActiveDTO dto){
        try {
            boolean actived =  userService.activeEmail(dto);
            if (actived ==false)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Bạn không thể xác nhận mã xác thực vui lòng thử lại sau"));
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(dto.getEmail());
            RequestContext context = RequestContext.get();
            Map<String, Object> response = new HashMap<>();
            String token = jwtTokenUtil.generateToken(userDetails);
            ResponseCookie cookie = ResponseCookie.from("token", token)
                    .httpOnly(true)
                    .maxAge(Duration.ofHours(24))
                    .sameSite("Strict")
                    .secure(true)
                    .path("/")
                    .build();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
            response.put("token", token);
            if (context != null) {
                response.put("requestId", context.getRequestId());
                response.put("userId", context.getUserId());
                response.put("timestamp", context.getTimestamp());
                response.put("role", context.getRole());
                loginSessionService.createAndCheckLoginSession(context.getUserId(),context.getIpAddress(),context.getHostName(), context.getUserAgent());
            }
            tokenService.saveToken(token);
            return ResponseEntity.ok().headers(headers).body(response);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Có lỗi không mong muốn: " + e.getMessage()));
        }
    }
@PostMapping("/resendEmail")
public ResponseEntity<?> ResendEmail (@RequestParam("email") String email){
        try {
            userService.reSendEmail(email);
            return ResponseEntity.ok().body("Đã gửi lại mã xác thực");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
}
    @PostMapping("/login")
    public ResponseEntity<?>  login(@RequestParam("username") String username, @RequestParam("password") String password) {
        try {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            boolean isMatch = BCrypt.checkpw(password, userDetails.getPassword());
            if (!isMatch) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Mật khẩu không chính xác " ));
            }
            RequestContext context = RequestContext.get();
            String token = jwtTokenUtil.generateToken(userDetails);

            Map<String, Object> response = new HashMap<>();
            ResponseCookie cookie = ResponseCookie.from("token", token)
                    .httpOnly(true)
                    .maxAge(Duration.ofHours(24))
                    .sameSite("Strict")
                    .secure(true)
                    .path("/")
                    .build();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
            response.put("token", token);
            if (context != null) {
                response.put("requestId", context.getRequestId());
                response.put("userId", context.getUserId());
                response.put("timestamp", context.getTimestamp());
                response.put("role", context.getRole());
                loginSessionService.createAndCheckLoginSession(context.getUserId(),context.getIpAddress(),context.getHostName(), context.getUserAgent());
            }
            tokenService.saveToken(token);

            return ResponseEntity.ok().headers(headers).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/status")
    public AuthStatus getAuthStatus() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAuthenticated = !(principal instanceof String && principal.equals("anonymousUser"));

        String role = "";
        if (isAuthenticated) {
            UserDetails userDetails = (UserDetails) principal;
            role = userDetails.getAuthorities().toString();
        }
        return new AuthStatus(isAuthenticated, role);
    }

    @GetMapping("/forgetpassword/{username}")
    public ResponseEntity<?> forgetPassword(@PathVariable String username) {
        try {
            UserDTO dto = userService.foGetPasword(username);
            dto.setPassword(null);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("userDto", dto));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/resetpassword")
    public ResponseEntity<?> resetPassword(@RequestParam("email") String email, @RequestParam("newPassword") String newPassword,
                                           @RequestParam("confirmPassword") String confirmPassword) {
        try {
            boolean reset = userService.resetPassword(email,newPassword,confirmPassword);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", reset));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/password/resendEmail")
    public ResponseEntity<?> ResendEmailFotGetPassword(@RequestParam("email") String email){
        try {
            userService.reSendEmailForgetPassword(email);
            return ResponseEntity.ok().body("Đã gửi lại mã xác thực");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    @PostMapping("/active-email-for-get-password")
    public ResponseEntity<?> ActiveEmailFotGetPassword(@RequestBody PasswordResetDTO passwordResetDTO){
        try {
            userService.activeEmailForgetPassword(passwordResetDTO);
            return ResponseEntity.ok().body("Đã gửi lại mã xác thực");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }


    @PostMapping("/identify")
    public ResponseEntity<?> identifyPassword(@RequestBody PasswordResetDTO passwordResetDTO) {
        try {
            UserDTO reset = userService.identifyPassword(passwordResetDTO);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", reset));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Logout failed: bạn chưa đăng nhập!");
            }
            String jwtToken = extractJwtToken(request);
            if (jwtToken != null) {
                // Thực hiện các thao tác cần thiết với token, chẳng hạn như xóa khỏi cơ sở dữ liệu
                tokenService.logoutToken(jwtToken); // Thay vì saveToken, bạn nên có removeToken
            }
            return ResponseEntity.ok("Logout successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Logout failed: " + e.getMessage());
        }
    }

    private String extractJwtToken(HttpServletRequest request) {
        final String requestTokenHeader = request.getHeader("Authorization");
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            return requestTokenHeader.substring(7);
        }
        return null;
    }

}
