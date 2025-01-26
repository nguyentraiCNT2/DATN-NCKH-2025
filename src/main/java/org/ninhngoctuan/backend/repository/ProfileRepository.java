package org.ninhngoctuan.backend.repository;

import org.ninhngoctuan.backend.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
    @Query("select pr FROM ProfileEntity pr WHERE pr.user.userId = :userId")
    Optional<ProfileEntity> findByUser(@Param("userId") Long userId);
}
