package org.ninhngoctuan.backend.repository;

import org.ninhngoctuan.backend.entity.MessageAudiosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageAudiosRepository extends JpaRepository<MessageAudiosEntity, Long> {
    @Query("select  m from MessageAudiosEntity m where m.message.messageId = :messageId")
    List<MessageAudiosEntity> findByMessageId(@Param("messageId") Long messageId);
}