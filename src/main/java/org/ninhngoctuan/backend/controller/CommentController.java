package org.ninhngoctuan.backend.controller;

import org.ninhngoctuan.backend.dto.CommentDTO;
import org.ninhngoctuan.backend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @GetMapping("/getbypost/{postid}")
    public ResponseEntity<?> getByPost(@PathVariable Long postid) {
        try {
            List<CommentDTO> commentDTOList = commentService.getComments(postid);
            return ResponseEntity.ok(commentDTOList);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

        }
    }
    @PostMapping("/create")
    public ResponseEntity<?> createComment(@RequestBody CommentDTO commentDTO) {
        try {
            CommentDTO dto = commentService.createCommend(commentDTO);
            return ResponseEntity.ok(dto);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PutMapping("/update/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId,@RequestBody CommentDTO commentDTO) {
        try {
            commentDTO.setCommentId(commentId);
            CommentDTO dto = commentService.updateCommend(commentDTO);
            return ResponseEntity.ok(dto);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        try {
             commentService.deleteCommend(commentId);
            return ResponseEntity.ok("Xóa thành công");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
