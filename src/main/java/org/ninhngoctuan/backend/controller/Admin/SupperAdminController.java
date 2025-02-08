package org.ninhngoctuan.backend.controller.Admin;

import org.ninhngoctuan.backend.dto.UserDTO;
import org.ninhngoctuan.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/superadmin")
public class SupperAdminController {

    private final UserService userService;

    public SupperAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/getall")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserDTO> list = userService.getAllSupperAdmin();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/user/lock/{userId}")
    public ResponseEntity<?> lockUsers(@PathVariable Long userId) {
        try {
             userService.lockUser(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/user/unlock/{userId}")
    public ResponseEntity<?> unlockUsers(@PathVariable Long userId) {
        try {
            userService.unlockUser(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/{userId}/update/role/{roleId}")
    public ResponseEntity<?> updateRole(@PathVariable Long userId, @PathVariable Long roleId) {
        try {
            userService.updateRole(userId, roleId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
