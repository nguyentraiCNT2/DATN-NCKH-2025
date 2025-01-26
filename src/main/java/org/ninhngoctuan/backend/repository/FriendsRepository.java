package org.ninhngoctuan.backend.repository;

import org.ninhngoctuan.backend.entity.FriendEntity;
import org.ninhngoctuan.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendsRepository extends JpaRepository<FriendEntity, Long> {
    Optional<FriendEntity> findByFriendshipId(Long id);
    List<FriendEntity> findByUser(UserEntity user);
    List<FriendEntity> findByUserAndStatus(UserEntity  user,String status);
    List<FriendEntity> findByFriendAndStatus(UserEntity  friend,String status);
    FriendEntity findByUserAndFriend(UserEntity user,UserEntity friend);
    List<FriendEntity> findByFriend(UserEntity friend);

    FriendEntity findByUserAndFriendAndStatus(UserEntity user,UserEntity friend,String status);
}
