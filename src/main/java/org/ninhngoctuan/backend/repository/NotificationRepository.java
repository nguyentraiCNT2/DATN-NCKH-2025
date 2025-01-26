package org.ninhngoctuan.backend.repository;

import org.ninhngoctuan.backend.entity.NotificationEntity;
import org.ninhngoctuan.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity,Long> {
    List<NotificationEntity> findByUserOrderByCreatedAtDesc(UserEntity user);
    @Query(value = "SELECT COUNT(*) FROM NotificationEntity n WHERE n.readStatus = false")
    Long countUnreadNotifications();
}
