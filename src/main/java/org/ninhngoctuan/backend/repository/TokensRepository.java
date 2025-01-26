package org.ninhngoctuan.backend.repository;

import org.ninhngoctuan.backend.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TokensRepository extends JpaRepository<TokenEntity, Long> {
    List<TokenEntity> findByToken(String token);
    boolean existsByToken(String token);
    void deleteByExpiresAtBefore(Date date);
    List<TokenEntity> findByUserId(Long userId);
}
