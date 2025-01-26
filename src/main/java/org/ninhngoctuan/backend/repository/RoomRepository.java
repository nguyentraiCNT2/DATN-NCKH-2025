package org.ninhngoctuan.backend.repository;

import org.ninhngoctuan.backend.entity.RoomEntity;
import org.ninhngoctuan.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    List<RoomEntity> findByMember1OrMember2(UserEntity user, UserEntity user2);
    List<RoomEntity> findByMember1AndMember2OrMember1AndMember2(UserEntity user, UserEntity user2, UserEntity user1, UserEntity user3);
}
