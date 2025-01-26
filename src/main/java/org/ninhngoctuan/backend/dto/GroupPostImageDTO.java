package org.ninhngoctuan.backend.dto;

import jakarta.persistence.*;
import org.ninhngoctuan.backend.entity.GroupPostEntity;
import org.ninhngoctuan.backend.entity.ImagesEntity;

public class GroupPostImageDTO {
    private Long id;
    private GroupPostDTO group;
    private ImagesDTO images;

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

    public ImagesDTO getImages() {
        return images;
    }

    public void setImages(ImagesDTO images) {
        this.images = images;
    }
}
