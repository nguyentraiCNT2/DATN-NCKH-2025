package org.ninhngoctuan.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "GroupPostImages")
public class GroupPostImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupPostEntity group;
    @ManyToOne
    @JoinColumn(name = "image_id")
    private ImagesEntity images;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GroupPostEntity getGroup() {
        return group;
    }

    public void setGroup(GroupPostEntity group) {
        this.group = group;
    }

    public ImagesEntity getImages() {
        return images;
    }

    public void setImages(ImagesEntity images) {
        this.images = images;
    }
}
