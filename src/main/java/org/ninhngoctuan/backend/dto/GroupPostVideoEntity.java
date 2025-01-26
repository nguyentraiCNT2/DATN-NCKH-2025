package org.ninhngoctuan.backend.dto;

import jakarta.persistence.*;
import org.ninhngoctuan.backend.entity.GroupPostEntity;
import org.ninhngoctuan.backend.entity.VideosEntity;

public class GroupPostVideoEntity {
    private Long id;
    private GroupPostDTO group;
    private VideosDTO videos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GroupPostDTO getGroup() {
        return group;
    }

    public void setGroup(GroupPostDTO group) {
        this.group = group;
    }

    public VideosDTO getVideos() {
        return videos;
    }

    public void setVideos(VideosDTO videos) {
        this.videos = videos;
    }
}
