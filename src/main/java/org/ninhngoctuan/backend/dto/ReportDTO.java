package org.ninhngoctuan.backend.dto;


import java.util.Date;

public class ReportDTO {
    private Long reportId;

    private UserDTO reportedBy;

    private PostDTO post;

    private String reason;

    private Date createdAt;

    // Getters and setters
}
