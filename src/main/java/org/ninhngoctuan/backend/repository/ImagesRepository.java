package org.ninhngoctuan.backend.repository;

import org.ninhngoctuan.backend.entity.ImagesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ImagesRepository extends JpaRepository<ImagesEntity,Long> {

}
