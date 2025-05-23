package org.ninhngoctuan.backend.repository;

import org.ninhngoctuan.backend.entity.VideosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideosRepository  extends JpaRepository<VideosEntity, Long> {
}
