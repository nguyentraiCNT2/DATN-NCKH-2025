package org.ninhngoctuan.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "GroupPostVideos")
public class GroupPostVideoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupPostEntity group;
    @ManyToOne
    @JoinColumn(name = "video_id")
    private VideosEntity videos;

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

    public VideosEntity getVideos() {
        return videos;
    }

    public void setVideos(VideosEntity videos) {
        this.videos = videos;
    }
}
