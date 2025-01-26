package org.ninhngoctuan.backend.dto;

import jakarta.persistence.*;

import java.sql.Timestamp;

public class ThemmeDTO {
    private Long id;
    private String name;
    private String color;
    private String description;
    private String image;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
