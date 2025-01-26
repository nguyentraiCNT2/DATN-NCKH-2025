package org.ninhngoctuan.backend.dto;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

public class GroupDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;


    private String groupImage;
    private String groupCoverImage;

    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToMany(mappedBy = "group")
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
}
