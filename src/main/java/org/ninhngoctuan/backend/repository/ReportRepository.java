package org.ninhngoctuan.backend.repository;

import org.ninhngoctuan.backend.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity,Long> {
}
