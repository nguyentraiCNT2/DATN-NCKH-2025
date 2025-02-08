package org.ninhngoctuan.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {
    private Long reportId;

    private UserDTO reportedBy;

    private UserDTO user;

    private String reason;

    private Date createdAt;

    // Getters and setters
}
