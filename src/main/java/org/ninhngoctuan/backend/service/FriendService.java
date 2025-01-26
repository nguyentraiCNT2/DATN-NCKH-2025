package org.ninhngoctuan.backend.service;

import org.ninhngoctuan.backend.dto.FriendDTO;

import java.util.List;

public interface FriendService {
    List<FriendDTO> getFriends();
    List<FriendDTO> getFollower();
    List<FriendDTO> getByStatus();
    FriendDTO addFriend(FriendDTO friendDTO);
    FriendDTO blockFriend(FriendDTO friendDTO);
    FriendDTO unblockFriend(FriendDTO friendDTO);
    void  cancelFriend(FriendDTO friendDTO);
    boolean checkFriend(Long id);
}
