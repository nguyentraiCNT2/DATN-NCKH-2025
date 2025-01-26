package org.ninhngoctuan.backend.controller;

import org.ninhngoctuan.backend.dto.GroupDTO;
import org.ninhngoctuan.backend.dto.GroupPostDTO;
import org.ninhngoctuan.backend.dto.UserDTO;
import org.ninhngoctuan.backend.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    // Lấy danh sách nhóm của người dùng
    @GetMapping("/user")
    public ResponseEntity<?> getGroupsByUserId() {
        try {
           List<GroupDTO> groupDTOList = groupService.getByUserId();
            return ResponseEntity.ok(groupDTOList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/user/all-post")
    public ResponseEntity<?> getAllGroupsPostByUserId() {
        try {
            List<GroupPostDTO>  list = groupService.getAllPostsByGroup();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getGroupsById(@PathVariable Long id) {
        try {
            GroupDTO groupDTOList = groupService.getById(id);
            return ResponseEntity.ok(groupDTOList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Lấy bài viết đã được duyệt theo groupId
    @GetMapping("/{groupId}/posts/approved")
    public ResponseEntity<?> getApprovedPostsByGroupId(@PathVariable Long groupId) {
        try {
            return ResponseEntity.ok(groupService.getPostsByGroupId(groupId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Lấy bài viết chưa duyệt theo groupId
    @GetMapping("/{groupId}/posts/not-approved")
    public ResponseEntity<?> getNotApprovedPostsByGroupId(@PathVariable Long groupId) {
        try {
            return ResponseEntity.ok(groupService.getPostsByGroupIdNotApproved(groupId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Lấy danh sách thành viên nhóm
    @GetMapping("/{groupId}/members")
    public ResponseEntity<?> getAllMembers(@PathVariable Long groupId) {
        try {
            return ResponseEntity.ok(groupService.getAllMembers(groupId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> addGroup(@RequestBody GroupDTO groupDTO) {
        try {
            return ResponseEntity.ok(groupService.addGroup(groupDTO));  // Nếu tạo nhóm thành công
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  // Nếu có lỗi xảy ra
        }
    }

    // Cập nhật thông tin nhóm
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateGroup(@PathVariable Long id, @RequestBody GroupDTO groupDTO) {
        try {
            groupDTO.setGroupId(id);
            return ResponseEntity.ok(groupService.updateGroup(groupDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    // Xóa nhóm
    @DeleteMapping("/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long groupId) {
        try {
            return ResponseEntity.ok(groupService.deleteGroup(groupId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    // Thêm thành viên vào nhóm
    @PostMapping("/{groupId}/members/{memberId}")
    public ResponseEntity<?> addMember(@PathVariable Long groupId, @PathVariable Long memberId) {
        try {
            return ResponseEntity.ok(groupService.addMember(groupId, memberId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    // Tham gia nhóm
    @PostMapping("/{groupId}/join")
    public ResponseEntity<?> joinGroup(@PathVariable Long groupId) {
        try {
            return ResponseEntity.ok(groupService.joinGroup(groupId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    // Xóa thành viên khỏi nhóm
    @DeleteMapping("/{groupId}/members/{memberId}")
    public ResponseEntity<?> quitMember(@PathVariable Long groupId, @PathVariable Long memberId) {
        try {
            return ResponseEntity.ok(groupService.quitMember(groupId, memberId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    // Thêm bài viết vào nhóm
    @PostMapping("/{groupId}/posts")
    public ResponseEntity<?> addPost(@RequestParam("groupId") Long groupId,
                                     @RequestParam(value = "content", required = false) String content,
                                     @RequestParam(value = "images", required = false) List<MultipartFile> images,
                                     @RequestParam(value = "videos", required = false) List<MultipartFile> videos) {
        try {
            GroupDTO groupDTO = new GroupDTO();
            groupDTO.setGroupId(groupId);
            GroupPostDTO groupPostDTO = new GroupPostDTO();
            groupPostDTO.setContent(content);
            groupPostDTO.setGroup(groupDTO);
            return ResponseEntity.ok(groupService.addPost(groupPostDTO, images, videos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    // Xóa bài viết
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        try {
            return ResponseEntity.ok(groupService.deletePost(postId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    // Kích hoạt bài viết
    @PutMapping("/posts/{postId}/activate")
    public ResponseEntity<?> activePost(@PathVariable Long postId) {
        try {
            return ResponseEntity.ok(groupService.activePost(postId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    // Upload Group Image
    @PostMapping("/{groupId}/upload-image")
    public ResponseEntity<String> uploadGroupImage(
            @PathVariable Long groupId,
            @RequestParam("file") MultipartFile file) {
        try {
            boolean result = groupService.uploadImage(groupId, file);
            if (result) {
                return ResponseEntity.ok("Ảnh nhóm đã được tải lên thành công.");
            }
            return ResponseEntity.badRequest().body("Không thể tải ảnh nhóm.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Upload Cover Image
    @PostMapping("/{groupId}/upload-cover-image")
    public ResponseEntity<String> uploadCoverImage(
            @PathVariable Long groupId,
            @RequestParam("file") MultipartFile file) {
        try {
            boolean result = groupService.uploadCoverImage(groupId, file);
            if (result) {
                return ResponseEntity.ok("Ảnh bìa nhóm đã được tải lên thành công.");
            }
            return ResponseEntity.badRequest().body("Không thể tải ảnh bìa nhóm.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
