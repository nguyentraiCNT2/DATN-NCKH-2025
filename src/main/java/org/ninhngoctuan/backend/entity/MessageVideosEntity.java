package org.ninhngoctuan.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "MessageVideos")
public class MessageVideosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "messageId")
    private MessageEntity message;
    @ManyToOne
    @JoinColumn(name = "videoId")
    private VideosEntity video;

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

    public VideosEntity getVideo() {
        return video;
    }

    public void setVideo(VideosEntity video) {
        this.video = video;
    }
}
