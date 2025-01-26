package org.ninhngoctuan.backend.repository;

import org.ninhngoctuan.backend.entity.MessageEntity;
import org.ninhngoctuan.backend.entity.MessageVideosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageVideosRepository extends JpaRepository<MessageVideosEntity, Long> {
    List<MessageVideosEntity> findByMessage(MessageEntity message);
}
