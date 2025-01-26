package org.ninhngoctuan.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "MessageImages")
public class MessageImagesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "messageId")
    private MessageEntity message;
    @ManyToOne
    @JoinColumn(name = "imagesId")
    private ImagesEntity imagesEntity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MessageEntity getMessage() {
        return message;
    }

    public void setMessage(MessageEntity message) {
        this.message = message;
    }

    public ImagesEntity getImagesEntity() {
        return imagesEntity;
    }

    public void setImagesEntity(ImagesEntity imagesEntity) {
        this.imagesEntity = imagesEntity;
    }
}
