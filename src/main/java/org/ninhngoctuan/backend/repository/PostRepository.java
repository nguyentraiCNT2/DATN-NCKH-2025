package org.ninhngoctuan.backend.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.ninhngoctuan.backend.entity.PostEntity;
import org.ninhngoctuan.backend.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<PostEntity , Long> {
   Optional<PostEntity>  findByPostId(Long id);
    @Query("SELECT p FROM PostEntity p ORDER BY function('RAND')")
    List<PostEntity> findAllRandom();
    @Query("SELECT p FROM PostEntity p WHERE p.groupId.groupId = :groupId AND p.isDeleted = false ORDER BY p.createdAt DESC")
    List<PostEntity> findPostByGroupId(@Param("groupId") Long groupId);
    List<PostEntity> findAllByOrderByCreatedAtDesc();
    List<PostEntity> findByUserOrderByCreatedAtDesc(UserEntity user);
    @Query("SELECT p FROM PostEntity p WHERE p.user = :user AND p.isDeleted = false ORDER BY p.createdAt DESC")
    List<PostEntity> findAllByUserAndDeletedFalseOrderByCreatedAtDesc(@Param("user") UserEntity user);


}
