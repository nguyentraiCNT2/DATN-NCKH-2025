package org.ninhngoctuan.backend.controller.User;

import org.ninhngoctuan.backend.dto.*;
import org.ninhngoctuan.backend.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final FriendService friendService;
    private final UserService userService;
    private final PostService postService;
    private final GroupService groupService;
    private final TagService tagService;

    public HomeController(FriendService friendService, UserService userService, PostService postService, GroupService groupService, TagService tagService) {
        this.friendService = friendService;
        this.userService = userService;
        this.postService = postService;
        this.groupService = groupService;
        this.tagService = tagService;
    }

    @GetMapping("/friend/list")
    public ResponseEntity<?> getFriend(){
        try {
            List<FriendDTO> list =  friendService.getFriends();
            return new ResponseEntity<>(list, HttpStatus.OK);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/group/user")
    public ResponseEntity<?> getGroupsByUserId() {
        try {
            List<GroupDTO> groupDTOList = groupService.getByUserId();
            return ResponseEntity.ok(groupDTOList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/user/me")
    public ResponseEntity<?> me() {
        try {
            ProfileDTO dto = userService.me();
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PostMapping("/post/create")
    public ResponseEntity<?> createPost(
            @RequestParam("content") String content,
            @RequestParam(value = "tagId", required = false) Long tagId,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @RequestParam(value = "videos", required = false) List<MultipartFile> videos) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDTO userDTO = userService.info(authentication.getName());

            PostDTO postDTO = new PostDTO();
            postDTO.setContent(content);
            postDTO.setUser(userDTO);
            TagDTO tagDTO = new TagDTO();
            tagDTO.setTagId(tagId);
            postDTO.setTagId(tagDTO);

            PostDTO post = postService.createPost(postDTO, images, videos);
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Có lỗi không mong muốn: " + e.getMessage()));
        }
    }
    @GetMapping("/post/getbyfollow")
    public ResponseEntity<?> getbyfollow(@RequestParam(value = "page", defaultValue = "1") int page,
                                         @RequestParam(value = "size", defaultValue = "10") int size){
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<PostDTO> list = postService.getPostByFriend(pageable);

            return ResponseEntity.status(HttpStatus.OK).body(list);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error",  e.getMessage()));
        }
    }
    @GetMapping("/post/get-all")
    public ResponseEntity<?> getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "size", defaultValue = "10") int size){
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<PostDTO> list = postService.getAllPostDesc(pageable);

            return ResponseEntity.status(HttpStatus.OK).body(list);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Có lỗi không mong muốn: " + e.getMessage()));
        }
    }
    @GetMapping("/tag/get-all")
    public ResponseEntity<?> getAllTags(){
        try {
            List<TagDTO> tagDTOS = tagService.getAll();
            return  ResponseEntity.status(HttpStatus.OK).body(tagDTOS);
        }catch (Exception e){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
