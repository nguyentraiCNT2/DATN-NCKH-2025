package org.ninhngoctuan.backend.controller.Admin;

import org.ninhngoctuan.backend.dto.PostDTO;
import org.ninhngoctuan.backend.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/posts")
public class AdminPostController {
    private final PostService postService;

    public AdminPostController(PostService postService) {
        this.postService = postService;
    }
    @GetMapping("/get-all")
    public ResponseEntity<?> getAllPosts() {
        try {
            List<PostDTO> list = postService.getAllPostADMIN();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
