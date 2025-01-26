package org.ninhngoctuan.backend.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "PostVideo")
public class PostVideoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "postId")
    private PostEntity post;
    @ManyToOne
    @JoinColumn(name = "videosId")
    private VideosEntity videos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }

    public VideosEntity getVideos() {
        return videos;
    }

    public void setVideos(VideosEntity videos) {
        this.videos = videos;
    }
}
