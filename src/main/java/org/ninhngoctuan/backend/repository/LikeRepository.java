package org.ninhngoctuan.backend.repository;

import org.ninhngoctuan.backend.entity.LikeEntity;
import org.ninhngoctuan.backend.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    List<LikeEntity> findByPost(PostEntity post);


}
