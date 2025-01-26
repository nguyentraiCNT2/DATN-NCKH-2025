package org.ninhngoctuan.backend.repository;

import org.ninhngoctuan.backend.entity.GroupEntity;
import org.ninhngoctuan.backend.entity.GroupMemberEntity;
import org.ninhngoctuan.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMemberEntity, Long> {
    List<GroupMemberEntity> findByGroup(GroupEntity group);
    List<GroupMemberEntity> findByUser(UserEntity user);
    @Query("SELECT gm FROM GroupMemberEntity gm WHERE gm.group.groupId = :groupId AND gm.user.userId = :userId")
    GroupMemberEntity findByGroupIdAndUserId(@Param("groupId") Long groupId, @Param("userId") Long userId);
}
