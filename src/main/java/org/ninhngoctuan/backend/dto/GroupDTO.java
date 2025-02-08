package org.ninhngoctuan.backend.dto;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

public class GroupDTO {
    private Long groupId;

    private String name;

    private String description;

    private Date createdAt;


    private String groupImage;
    private String groupCoverImage;

    private Date updatedAt;

    private boolean deleted;

    private Set<GroupMemberDTO> groupMembers;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<GroupMemberDTO> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(Set<GroupMemberDTO> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public String getGroupCoverImage() {
        return groupCoverImage;
    }

    public void setGroupCoverImage(String groupCoverImage) {
        this.groupCoverImage = groupCoverImage;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
