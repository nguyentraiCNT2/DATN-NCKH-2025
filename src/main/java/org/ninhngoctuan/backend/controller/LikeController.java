package org.ninhngoctuan.backend.controller;

import org.ninhngoctuan.backend.dto.LikeDTO;
import org.ninhngoctuan.backend.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/likes")
public class LikeController {
    @Autowired
    private LikeService  likeService;
    @GetMapping("/list/post/{postId}")
    public ResponseEntity<?> ListLikeByPost(@PathVariable Long postId) {
        try {
            List<LikeDTO> likeDTOS = likeService.getLikesByPost(postId);
            return new ResponseEntity<>(likeDTOS, HttpStatus.OK);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("check/like/{postId}")
    public ResponseEntity<?> checkLike(@PathVariable Long postId) {
        try {
            boolean checkLike = likeService.checkLike(postId);
            return new ResponseEntity<>(checkLike, HttpStatus.OK);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("like/post/{postId}")
    public ResponseEntity<?> LikePost(@PathVariable Long postId) {
        try {
            LikeDTO likeDTOS = likeService.likePost(postId);
            return new ResponseEntity<>(likeDTOS, HttpStatus.OK);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}
