package org.ninhngoctuan.backend.repository;

import org.ninhngoctuan.backend.entity.AudiosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AudiosRepository extends JpaRepository<AudiosEntity, Long> {
}