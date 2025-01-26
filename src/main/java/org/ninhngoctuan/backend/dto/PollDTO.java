package org.ninhngoctuan.backend.dto;

import java.util.Date;
import java.util.Set;

public class PollDTO {
    private Long pollId;

    private UserDTO user;

    private String question;

    private Date createdAt;

    private Date updatedAt;

    private Set<PollOptionDTO> options;

    // Getters and setters
}
