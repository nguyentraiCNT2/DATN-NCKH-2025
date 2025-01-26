package org.ninhngoctuan.backend.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.ninhngoctuan.backend.entity.GroupPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface GroupPostRepository extends JpaRepository<GroupPostEntity, Long> {
    @Query("select gp from GroupPostEntity gp WHERE gp.group.groupId = :groupId order by gp.createdAt desc ")
    List<GroupPostEntity> findByGroupId(@Param("groupId") Long groupId);
}
