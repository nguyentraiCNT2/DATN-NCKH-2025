package org.ninhngoctuan.backend.controller;

import org.ninhngoctuan.backend.config.CustomWebSocketHandler;
import org.ninhngoctuan.backend.dto.*;
import org.ninhngoctuan.backend.entity.MessageEntity;
import org.ninhngoctuan.backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/messages")
public class MessageController {
    @Value("${images.dir}")
    private String imagesDir;
    @Value("${videos.dir}")
    private String videosDir;
    @Autowired
    private MessageService messageService;
    @Autowired
    private CustomWebSocketHandler customWebSocketHandler;
        // Gửi tin nhắn mới
        @PostMapping("/send")
        public ResponseEntity<?> sendMessage(  @RequestParam(value = "content",required = false) String content,
                                               @RequestParam("receiverId") Long receiverId,
                                               @RequestParam(value = "images", required = false) List<MultipartFile> images,
                                               @RequestParam(value = "videos", required = false) List<MultipartFile> videos) {
            try {
                UserDTO userDTO = new UserDTO();
                userDTO.setUserId(receiverId);
                MessageDTO messageDTO = new MessageDTO();
                messageDTO.setContent(content);
                messageDTO.setReceiver(userDTO);
                MessageDTO savedMessage = messageService.sendMessage(messageDTO, images, videos);
                // Gửi tin nhắn qua WebSocket
//                messagingTemplate.convertAndSend("/topic/room/" + savedMessage.getRoom().getId(), savedMessage);

                // Gửi tin nhắn qua WebSocket bằng CustomWebSocketHandler
                customWebSocketHandler.broadcastMessage(savedMessage);  // Giả sử bạn sẽ gửi qua tất cả các kết nối WebSocket

                return ResponseEntity.ok(savedMessage);
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }

        }

    // Lấy tin nhắn giữa hai người dùng
    @GetMapping("/between/{roomid}")
    public ResponseEntity<?> getMessagesBetweenUsers( @PathVariable Long roomid) {
        try {
            List<MessageDTO> messages = messageService.getMessagesBetweenUsers(roomid);
            return ResponseEntity.ok(messages);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/message/room")
    public ResponseEntity<?> getRoomByUser(){
        try {
            List<RoomDTO> list = messageService.getRoomsBetweenUsers();
            return ResponseEntity.ok(list);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/images/{filename}")
    public ResponseEntity<?> getImageByFilename(@PathVariable String filename) {
        try {
            File file = Paths.get(imagesDir).resolve(filename).toFile();
            if (file.exists()) {
                Resource resource = new FileSystemResource(file);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                headers.setContentType(MediaType.IMAGE_PNG);
                headers.setContentType(MediaType.IMAGE_GIF);
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
    @GetMapping("/{messageId}/image")
    public ResponseEntity<?> getImageByPost(@PathVariable Long messageId) {
        try {
            List<MessageImagesDTO> messageImagesDTOS = messageService.getImagesBetweenMessages(messageId);
            return ResponseEntity.status(HttpStatus.OK).body(messageImagesDTOS);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping("/{messageId}/video")
    public ResponseEntity<?> getVideosByPost(@PathVariable Long messageId) {
        try {
            List<MessageVideosDTO> messageVideosDTOS = messageService.getVideoBetweenMessages(messageId);
            return ResponseEntity.status(HttpStatus.OK).body(messageVideosDTOS);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/getroomfriend/{id}")
    public ResponseEntity<?> getRoomFriend(@PathVariable Long id) {
            try {
                RoomDTO dto = messageService.getRoomFriend(id);
                return ResponseEntity.ok(dto);
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            }

    }
}
