package org.ninhngoctuan.backend.entity;

import jakarta.persistence.*;
import lombok.*;


import java.util.Date;

@Entity
@Table(name = "audios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AudiosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "update_at")
    private Date updateAt;

    // Getters and Setters
}
