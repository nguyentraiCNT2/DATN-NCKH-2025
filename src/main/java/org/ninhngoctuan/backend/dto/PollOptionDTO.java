package org.ninhngoctuan.backend.dto;


import jakarta.persistence.*;

import java.sql.Date;

public class PollOptionDTO {
    private Long pollOptionId;

    private PollDTO poll;

    private String optionText;

    private Date createdAt;

    // Getters and setters
}
