package org.ninhngoctuan.backend.dto;

import jakarta.persistence.*;
import org.ninhngoctuan.backend.entity.MessageEntity;
import org.ninhngoctuan.backend.entity.VideosEntity;

public class MessageVideosDTO {
    private Long id;
    private MessageDTO message;
    private VideosDTO video;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MessageDTO getMessage() {
        return message;
    }

    public void setMessage(MessageDTO message) {
        this.message = message;
    }

    public VideosDTO getVideo() {
        return video;
    }

    public void setVideo(VideosDTO video) {
        this.video = video;
    }
}
