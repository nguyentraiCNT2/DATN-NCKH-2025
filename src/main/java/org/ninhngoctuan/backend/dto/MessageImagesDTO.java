package org.ninhngoctuan.backend.dto;

import jakarta.persistence.*;
import org.ninhngoctuan.backend.entity.ImagesEntity;
import org.ninhngoctuan.backend.entity.MessageEntity;

public class MessageImagesDTO {
    private Long id;
    private MessageDTO message;
    private ImagesDTO imagesEntity;

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

    public ImagesDTO getImagesEntity() {
        return imagesEntity;
    }

    public void setImagesEntity(ImagesDTO imagesEntity) {
        this.imagesEntity = imagesEntity;
    }
}
