package org.ninhngoctuan.backend.repository;

import org.ninhngoctuan.backend.entity.LoginSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface LoginSessionRepository extends JpaRepository<LoginSessionEntity, Long> {
    List<LoginSessionEntity> findByUserid(Long userId);
}
