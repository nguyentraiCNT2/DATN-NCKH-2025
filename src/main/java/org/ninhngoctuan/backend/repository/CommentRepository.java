package org.ninhngoctuan.backend.repository;

import org.ninhngoctuan.backend.entity.CommentEntity;
import org.ninhngoctuan.backend.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    Optional<CommentEntity> findByCommentId(Long commentId);
    List<CommentEntity> findByPost(PostEntity post);
}
