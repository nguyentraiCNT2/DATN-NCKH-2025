package org.ninhngoctuan.backend.dto;

import jakarta.persistence.*;
import org.ninhngoctuan.backend.entity.PostEntity;
import org.ninhngoctuan.backend.entity.VideosEntity;

public class PostVideoDTO {
    private Long id;
    private PostDTO post;
    private VideosDTO videos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PostDTO getPost() {
        return post;
    }

    public void setPost(PostDTO post) {
        this.post = post;
    }

    public VideosDTO getVideos() {
        return videos;
    }

    public void setVideos(VideosDTO videos) {
        this.videos = videos;
    }
}
