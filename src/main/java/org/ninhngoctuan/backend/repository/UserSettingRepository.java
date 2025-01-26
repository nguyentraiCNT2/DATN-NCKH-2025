package org.ninhngoctuan.backend.repository;

import org.ninhngoctuan.backend.entity.UserSettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSettingRepository extends JpaRepository<UserSettingEntity, Long> {
}
