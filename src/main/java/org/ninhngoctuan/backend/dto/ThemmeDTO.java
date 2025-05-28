package org.ninhngoctuan.backend.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThemmeDTO {
    private Long id;
    private String name;
    private String color;
    private String description;
    private String image;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
