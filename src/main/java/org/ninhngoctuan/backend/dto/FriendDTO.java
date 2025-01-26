package org.ninhngoctuan.backend.dto;

import java.util.Date;

public class FriendDTO {
    private Long friendshipId;

    private UserDTO user;

    private UserDTO friend; 

    private String status;

    private Date createdAt;

    public enum FriendshipStatus {
        PENDING, ACCEPTED, REJECTED
    }

    public Long getFriendshipId() {
        return friendshipId;
    }

    public void setFriendshipId(Long friendshipId) {
        this.friendshipId = friendshipId;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public UserDTO getFriend() {
        return friend;
    }

    public void setFriend(UserDTO friend) {
        this.friend = friend;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    // Getters and setters
}
