package org.ninhngoctuan.backend.controller;


import org.ninhngoctuan.backend.Response.RelationshipRespone;
import org.ninhngoctuan.backend.dto.RelationDTO;
import org.ninhngoctuan.backend.dto.UserDTO;
import org.ninhngoctuan.backend.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/relations")
public class RelationController {

    private final RelationService relationService;

    @Autowired
    public RelationController(RelationService relationService) {
        this.relationService = relationService;
    }

    // Lấy danh sách mối quan hệ của người dùng hiện tại
    @GetMapping("/me")
    public ResponseEntity<?> getByMe() {
        try {
            List<RelationshipRespone> relations = relationService.getByMe();
            return ResponseEntity.ok(relations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // Lấy danh sách lời mời kết nối
    @GetMapping("/getall-connectionrequest")
    public ResponseEntity<?> Getallconnectionrequest() {
        try {
            List<RelationshipRespone> relations = relationService.getAllConnectionRequest();
            return ResponseEntity.ok(relations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // Lấy danh sách mối quan hệ theo userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<?>  getAllById(@PathVariable Long userId) {
        try {
            List<RelationshipRespone> relations = relationService.getAllById(userId);
            return ResponseEntity.ok(relations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

        @GetMapping("/get-all/user-relationship")
        public ResponseEntity<?> getAllUserRelationShip(){
            try {
                List<UserDTO> list = relationService.getAllUser();
                return ResponseEntity.ok(list);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        // Gửi yêu cầu kết nối
        @PostMapping("/send-request")
        public ResponseEntity<?> sendConnectionRequest(
                @RequestParam String name,
                @RequestParam Long secondPeopleId) {
            try {
                relationService.sendConnectionRequest(name, secondPeopleId);
                return ResponseEntity.ok("Yêu cầu kết nối đã được gửi");
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

    // Xác nhận yêu cầu kết nối
    @PutMapping("/confirm/{id}/user")
    public ResponseEntity<?> confirmConnection(@PathVariable Long id, @RequestParam("userId") Long userId) {
        try {
            relationService.confirmConnection(id,userId);
            return ResponseEntity.ok("Kết nối đã được xác nhận");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Từ chối yêu cầu kết nối
    @DeleteMapping("/reject/{id}")
    public ResponseEntity<?> rejectConnection(@PathVariable Long id) {
        try {
            relationService.rejectConnection(id);
            return ResponseEntity.ok("Yêu cầu kết nối đã bị từ chối");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Xóa kết nối
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteConnection(@PathVariable Long id) {
        try {
            relationService.deleteConnection(id);
            return ResponseEntity.ok("Kết nối đã được xóa");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}