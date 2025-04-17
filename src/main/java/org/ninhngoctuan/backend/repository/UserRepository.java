package org.ninhngoctuan.backend.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.ninhngoctuan.backend.entity.RoleEntity;
import org.ninhngoctuan.backend.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    UserEntity findByPhone(String phone);
    boolean existsByEmail(String email);
    Optional<UserEntity> findByUserId(Long userid);
    List<UserEntity> findByRoleId(RoleEntity role, Pageable pageable);
    @Query("SELECT u FROM UserEntity u WHERE " +
            "(:fullName IS NULL OR u.fullName LIKE %:fullName%) AND " +
            "(:email IS NULL OR u.email LIKE %:email%) AND " +
            "(:phone IS NULL OR u.phone LIKE %:phone%) AND " +
            "(:isActive IS NULL OR u.isActive = :isActive) AND " +
            "(:isEmailActive IS NULL OR u.isEmailActive = :isEmailActive)")
    List<UserEntity> filterUsers(
            @Param("fullName") String fullName,
            @Param("email") String email,
            @Param("phone") String phone,
            @Param("isActive") Boolean isActive,
            @Param("isEmailActive") Boolean isEmailActive,
            Pageable pageable
    );

    List<UserEntity> findByFullNameLike(String fullName);



}
