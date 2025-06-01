package org.ninhngoctuan.backend.repository;

import org.ninhngoctuan.backend.entity.RelationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationRepository extends JpaRepository<RelationEntity, Long> {
    @Query("select r from RelationEntity r where r.firstPeople.userId = :userId" +
            " or r.secondPeople.userId = :userId" +
            " AND r.status = true")
    List<RelationEntity> findByFirstPeopleIdOrSecondPeopleId(@Param("userId") Long userId);
    @Query("select r from RelationEntity r where r.firstPeople.userId = :userId" +
            " or r.secondPeople.userId = :userId" +
            " AND r.status = false")
    List<RelationEntity> findByConnectionRequest(@Param("userId") Long userId);
}
