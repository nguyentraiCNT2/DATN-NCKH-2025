package org.ninhngoctuan.backend.controller;

import org.ninhngoctuan.backend.context.RequestContext;
import org.ninhngoctuan.backend.dto.FriendDTO;
import org.ninhngoctuan.backend.dto.UserDTO;
import org.ninhngoctuan.backend.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friend")
public class FriendController {
    @Autowired
    private FriendService friendService;
    @GetMapping("/list")
    public ResponseEntity<?> getFriend(@RequestParam(value = "page", defaultValue = "1") int page,
                                       @RequestParam(value = "limit", defaultValue = "10") int limit){
        try {
            List<FriendDTO> list =  friendService.getFriends();
            return new ResponseEntity<>(list, HttpStatus.OK);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/list/follower")
    public ResponseEntity<?> getFollowers(){
        try {
            List<FriendDTO> list =  friendService.getFollower();
            return new ResponseEntity<>(list, HttpStatus.OK);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/block")
    public ResponseEntity<?> getBlockFriend(){
        try {
            List<FriendDTO> list =  friendService.getByStatus();
            return new ResponseEntity<>(list, HttpStatus.OK);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/add/{friendId}")
    public ResponseEntity<?> addFriend(@PathVariable Long friendId){
        try {
            RequestContext context = RequestContext.get();
            UserDTO friendDTO = new UserDTO();
            friendDTO.setUserId(friendId);
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(context.getUserId());
            FriendDTO dto = new FriendDTO();
            dto.setUser(userDTO);
            dto.setFriend(friendDTO);
            friendService.addFriend(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/cancel/{friendId}")
    public ResponseEntity<?> cancelFriend(@PathVariable Long friendId){
        try {
           UserDTO userDTO = new UserDTO();
           userDTO.setUserId(friendId);
           FriendDTO friendDTO  = new FriendDTO();
           friendDTO.setFriend(userDTO);
           friendService.cancelFriend(friendDTO);
           return  ResponseEntity.status(HttpStatus.OK).body("Hủy kết bạn thành công");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/block/{friendId}")
    public ResponseEntity<?> blockFriend(@PathVariable Long friendId){
        try {
            UserDTO friend = new UserDTO();
            friend.setUserId(friendId);
            FriendDTO dto = new FriendDTO();
            dto.setFriend(friend);
           FriendDTO friendDTO = friendService.blockFriend(dto);
            return ResponseEntity.status(HttpStatus.OK).body(friendDTO);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PutMapping("/unblock/{friendId}")
    public ResponseEntity<?> unBlockFriend(@PathVariable Long friendId){
        try {
            UserDTO friend = new UserDTO();
            friend.setUserId(friendId);
            FriendDTO dto = new FriendDTO();
            dto.setFriend(friend);
            FriendDTO friendDTO = friendService.unblockFriend(dto);
            return ResponseEntity.status(HttpStatus.OK).body(friendDTO);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/checkfriend/{friendId}")
    public ResponseEntity<?> checkFriend(@PathVariable Long friendId){
        try {
            boolean check = friendService.checkFriend(friendId);
            return ResponseEntity.status(HttpStatus.OK).body(check);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
