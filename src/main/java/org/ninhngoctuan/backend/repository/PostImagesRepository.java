package org.ninhngoctuan.backend.repository;

import org.ninhngoctuan.backend.entity.PostEntity;
import org.ninhngoctuan.backend.entity.PostImagesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostImagesRepository extends JpaRepository<PostImagesEntity,Long> {
    List<PostImagesEntity> findByPost(PostEntity postId);
}
