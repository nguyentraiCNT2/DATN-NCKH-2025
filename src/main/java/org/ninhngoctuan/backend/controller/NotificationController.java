package org.ninhngoctuan.backend.controller;

import org.ninhngoctuan.backend.dto.NotificationDTO;
import org.ninhngoctuan.backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/follow")
    public ResponseEntity<?> follow() {
        try {
            List<NotificationDTO>  notificationDTOList = notificationService.getNotificationsByFollow();
            return new ResponseEntity<>(notificationDTOList, HttpStatus.OK);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/total")
    public ResponseEntity<?> TotalRead() {
        try {
           Long total = notificationService.totalNotificationsNotRead();
            return new ResponseEntity<>(total, HttpStatus.OK);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PutMapping("/read/notification/{id}")
    public ResponseEntity<?> update(@PathVariable Long id) {
        try {
            NotificationDTO notificationDTO = notificationService.ReadNotification(id);
            return new ResponseEntity<>(notificationDTO, HttpStatus.OK);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PostMapping("/read/all/notification")
    public ResponseEntity<?> readAll() {
        try {
            boolean read = notificationService.readAllNotifications();
            return new ResponseEntity<>(read, HttpStatus.OK);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
