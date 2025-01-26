package org.ninhngoctuan.backend.repository;

import org.ninhngoctuan.backend.entity.PostEntity;
import org.ninhngoctuan.backend.entity.PostVideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostVideosRepository  extends JpaRepository<PostVideoEntity, Long> {
    List<PostVideoEntity> findByPost(PostEntity postId);
}
