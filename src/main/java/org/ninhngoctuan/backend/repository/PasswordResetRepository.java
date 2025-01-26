package org.ninhngoctuan.backend.repository;

import org.ninhngoctuan.backend.entity.PasswordResetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordResetEntity, Long> {
    List<PasswordResetEntity> findByEmail(String email);
}
