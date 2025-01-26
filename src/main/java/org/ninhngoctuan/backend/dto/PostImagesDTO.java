package org.ninhngoctuan.backend.dto;

import jakarta.persistence.*;
import org.ninhngoctuan.backend.entity.ImagesEntity;
import org.ninhngoctuan.backend.entity.PostEntity;

public class PostImagesDTO {
    private Long id;
    private PostDTO post;
    private ImagesDTO images;

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

    public ImagesDTO getImages() {
        return images;
    }

    public void setImages(ImagesDTO images) {
        this.images = images;
    }
}
