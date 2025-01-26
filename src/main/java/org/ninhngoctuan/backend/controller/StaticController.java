package org.ninhngoctuan.backend.controller;

import org.ninhngoctuan.backend.service.StaticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class StaticController {
    @Autowired
    private StaticService staticService;
    @GetMapping("/statis")
    public ResponseEntity<?> statis() {
        try {
            Map<String, String> response = new HashMap<>();
            int totalUser = staticService.totalUser();
            int totalPost = staticService.totalPost();
            int totalComment = staticService.totalComment();
            int totalLike = staticService.totalLike();
            int totalMessage = staticService.totalMessage();
            int totalImage = staticService.totalImage();
            int totalVideo = staticService.totalVideo();
            response.put("totalUser", String.valueOf(totalUser));
            response.put("totalPost", String.valueOf(totalPost));
            response.put("totalComment", String.valueOf(totalComment));
            response.put("totalLike", String.valueOf(totalLike));
            response.put("totalMessage", String.valueOf(totalMessage));
            response.put("totalImage", String.valueOf(totalImage));
            response.put("totalVideo", String.valueOf(totalVideo));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
