package org.ninhngoctuan.backend.service;

import org.ninhngoctuan.backend.dto.NotificationDTO;

import java.util.List;

public interface NotificationService {
    List<NotificationDTO> getNotificationsByFollow();
    Long totalNotificationsNotRead();
    NotificationDTO ReadNotification(Long id);
    void DeleteNotification(Long id);
    boolean readAllNotifications();
}
