package org.ninhngoctuan.backend.repository;

import org.ninhngoctuan.backend.entity.EmailActiveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailActiveRepository extends JpaRepository<EmailActiveEntity, Long> {
    List<EmailActiveEntity> findByEmail(String email);
}
