package org.ninhngoctuan.backend.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "Templates")
public class ThemmeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private String description;
    @Column(columnDefinition = "TEXT")
    private String image;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
