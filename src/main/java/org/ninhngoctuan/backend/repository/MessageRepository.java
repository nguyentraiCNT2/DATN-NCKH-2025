package org.ninhngoctuan.backend.repository;

import org.ninhngoctuan.backend.entity.MessageEntity;
import org.ninhngoctuan.backend.entity.RoomEntity;
import org.ninhngoctuan.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findBySenderAndReceiverOrSenderAndReceiver(UserEntity sender1, UserEntity receiver1, UserEntity sender2, UserEntity receiver2);
List<MessageEntity> findByRoom(RoomEntity room);
 Optional<MessageEntity>  findByMessageId(Long messageId);
}
