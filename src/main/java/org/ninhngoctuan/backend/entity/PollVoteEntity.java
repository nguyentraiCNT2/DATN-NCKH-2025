package org.ninhngoctuan.backend.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "PollVotes")
public class PollVoteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pollVoteId;

    @ManyToOne
    @JoinColumn(name = "poll_option_id", nullable = false)
    private PollOptionEntity pollOption;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    // Getters and setters
}
