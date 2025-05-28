package org.ninhngoctuan.backend.repository;

import org.ninhngoctuan.backend.entity.ThemmeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThemeRepository extends JpaRepository<ThemmeEntity, Long> {

}
