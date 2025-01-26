package org.ninhngoctuan.backend.dto;

import org.ninhngoctuan.backend.entity.GroupEntity;
import org.ninhngoctuan.backend.entity.TagEntity;

import java.util.Date;

public class PostDTO {
    private Long postId;

    private UserDTO user;

    private String content;

    private String imageUrl;

    private String videoUrl;

    private Date createdAt;

    private Date updatedAt;

    private TagDTO tagId;
    private GroupDTO groupId;
    private Long totalLike;
    private boolean isDeleted;
    private String university;
    private String relationship;
    private String nickName;
    private String description;
    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public TagDTO getTagId() {
        return tagId;
    }

    public void setTagId(TagDTO tagId) {
        this.tagId = tagId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Long getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(Long totalLike) {
        this.totalLike = totalLike;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GroupDTO getGroupId() {
        return groupId;
    }

    public void setGroupId(GroupDTO groupId) {
        this.groupId = groupId;
    }
}
