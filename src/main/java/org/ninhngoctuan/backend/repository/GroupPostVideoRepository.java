package org.ninhngoctuan.backend.repository;

import org.ninhngoctuan.backend.entity.GroupPostVideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface GroupPostVideoRepository extends JpaRepository<GroupPostVideoEntity, Long> {
}
