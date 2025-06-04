package org.ninhngoctuan.backend.controller;

import org.ninhngoctuan.backend.controller.input.PostInput;
import org.ninhngoctuan.backend.dto.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.ninhngoctuan.backend.service.PostService;
import org.ninhngoctuan.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Value("${images.dir}")
    private String imagesDir;
    @Value("${videos.dir}")
    private String videosDir;
    @PostMapping("/create")
    public ResponseEntity<?> createPost(
            @RequestParam("content") String content,
            @RequestParam(value = "tagId", required = false) Long tagId,
            @RequestParam(value = "groupId", required = false) Long groupId,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @RequestParam(value = "videos", required = false) List<MultipartFile> videos) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDTO userDTO = userService.info(authentication.getName());
            GroupDTO groupDTO = new GroupDTO();
            groupDTO.setGroupId(groupId);
            PostDTO postDTO = new PostDTO();
            postDTO.setContent(content);
            postDTO.setUser(userDTO);
            TagDTO tagDTO = new TagDTO();
            tagDTO.setTagId(tagId);
            postDTO.setTagId(tagDTO);
            postDTO.setGroupId(groupDTO);

            PostDTO post = postService.createPost(postDTO, images, videos);
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error",  e.getMessage()));
        }
    }


    @PostMapping("/update/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id,
            @RequestParam("content") String content,
            @RequestParam(value = "tagId",required = false) Long tagId,
            @RequestParam(value = "groupId", required = false) Long groupId,
            @RequestParam(value = "images", required = false)  List<MultipartFile> images,
            @RequestParam(value = "videos", required = false)  List<MultipartFile> videos) {
        try {
            GroupDTO groupDTO = new GroupDTO();
            groupDTO.setGroupId(groupId);
            PostDTO postDTO = new PostDTO();
            postDTO.setPostId(id);
            postDTO.setContent(content);
            TagDTO tagDTO = new TagDTO();
            tagDTO.setTagId(tagId);
            postDTO.setTagId(tagDTO);
            postDTO.setGroupId(groupDTO);
            PostDTO post = postService.updatePost(postDTO, images, videos);
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Có lỗi không mong muốn: " + e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        try {
            postService.deletePost(postId);
            return ResponseEntity.ok().body("Xóa thành công");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/show/{postId}")
    public ResponseEntity<?> showPost(@PathVariable Long postId) {
        try {
            postService.showPost(postId);
            return ResponseEntity.ok().body("hiển thị thành công");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body( e.getMessage());
        }
    }



    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            File file = Paths.get(imagesDir).resolve(filename).toFile();
            if (file.exists()) {
                Resource resource = new FileSystemResource(file);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG); // Set the correct media type
                headers.setContentType(MediaType.IMAGE_PNG); // Set the correct media type
                headers.setContentType(MediaType.IMAGE_GIF); // Set the correct media type
                headers.setContentDispositionFormData("attachment", filename);
                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/video/{filename}")
    public ResponseEntity<Resource> getVideos(@PathVariable String filename) {
        try {
            File file = Paths.get(videosDir).resolve(filename).toFile();
            if (file.exists()) {
                Resource resource = new FileSystemResource(file);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", filename);
                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getall")
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
    @GetMapping("/group/{id}")
    public ResponseEntity<?> getAll(@PathVariable Long id){
        try {

            List<PostDTO> list = postService.getAllPostByGroupId(id);

            return ResponseEntity.status(HttpStatus.OK).body(list);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Có lỗi không mong muốn: " + e.getMessage()));
        }
    }
    @GetMapping("/group/get-all")
    public ResponseEntity<?> groupgetAll(){
        try {

            List<PostDTO> list = postService.getAllPostByGroups();

            return ResponseEntity.status(HttpStatus.OK).body(list);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Có lỗi không mong muốn: " + e.getMessage()));
        }
    }
    @GetMapping("/getbyuser")
    public ResponseEntity<?> getByUser(){
        try {
            List<PostDTO> list = postService.getByUserid();

            return ResponseEntity.status(HttpStatus.OK).body(list);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Có lỗi không mong muốn: " + e.getMessage()));
        }
    }
    @GetMapping("/getbyuser/{id}")
    public ResponseEntity<?> getByUser(@PathVariable Long id){
        try {
            List<PostDTO> list = postService.getByUserid(id);

            return ResponseEntity.status(HttpStatus.OK).body(list);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Có lỗi không mong muốn: " + e.getMessage()));
        }
    }
    @GetMapping("/getbyfollow")
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
    @GetMapping("/{postId}/image")
    public ResponseEntity<?> getImageByPost(@PathVariable Long postId) {
        try {
            List<PostImagesDTO> postImagesDTOList = postService.getPostImages(postId);
            return ResponseEntity.status(HttpStatus.OK).body(postImagesDTOList);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping("/{postId}/video")
    public ResponseEntity<?> getVideosByPost(@PathVariable Long postId) {
        try {
            List<PostVideoDTO> postVideoDTOList = postService.getPostVideos(postId);
            return ResponseEntity.status(HttpStatus.OK).body(postVideoDTOList);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable Long postId) {
        try {
            PostDTO postDTO = postService.getById(postId);
            return ResponseEntity.status(HttpStatus.OK).body(postDTO);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}
