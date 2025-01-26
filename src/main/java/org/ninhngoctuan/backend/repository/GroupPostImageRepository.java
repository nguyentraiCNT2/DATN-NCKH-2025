package org.ninhngoctuan.backend.repository;

import org.ninhngoctuan.backend.entity.GroupPostImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupPostImageRepository  extends JpaRepository<GroupPostImageEntity, Long> {
}
