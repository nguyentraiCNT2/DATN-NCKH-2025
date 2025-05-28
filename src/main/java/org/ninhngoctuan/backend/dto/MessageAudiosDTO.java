package org.ninhngoctuan.backend.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ninhngoctuan.backend.entity.MessageEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageAudiosDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private MessageEntity message;

    @ManyToOne
    @JoinColumn(name = "audio_id")
    private AudiosDTO audio;

    // Getters and Setters
}