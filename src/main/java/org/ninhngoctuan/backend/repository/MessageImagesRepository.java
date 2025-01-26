package org.ninhngoctuan.backend.repository;

import org.ninhngoctuan.backend.entity.MessageEntity;
import org.ninhngoctuan.backend.entity.MessageImagesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface MessageImagesRepository extends JpaRepository<MessageImagesEntity,Long> {
    List<MessageImagesEntity> findByMessage(MessageEntity messageId);
}
