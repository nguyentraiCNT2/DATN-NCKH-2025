package org.ninhngoctuan.backend.entity;


import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "PollOptions")
public class PollOptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pollOptionId;

    @ManyToOne
    @JoinColumn(name = "poll_id", nullable = false)
    private PollEntity poll;

    @Column(nullable = false, length = 255)
    private String optionText;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    // Getters and setters
}
