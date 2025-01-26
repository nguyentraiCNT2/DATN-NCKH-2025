package org.ninhngoctuan.backend.dto;

import java.util.Date;

public class EventAttendeeDTO {
    private Long eventAttendeeId;

    private EventDTO event;

    private UserDTO user;

    private String rsvpStatus;

    private Date createdAt;

    public enum RSVPStatus {
        ACCEPTED, DECLINED, TENTATIVE
    }

    // Getters and setters
}
