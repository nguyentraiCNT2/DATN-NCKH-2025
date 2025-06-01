package org.ninhngoctuan.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "relations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    @Column(columnDefinition = "TEXT")
    private String name;
    @ManyToOne
    @JoinColumn(name = "first_people_id")
    private UserEntity firstPeople;
    @ManyToOne
    @JoinColumn(name = "second_people_id")
    private UserEntity secondPeople;
    @Column(name = "create_at")
    private Timestamp createAt;
    @Column(name = "update_at")
    private Timestamp updateAt;
    private boolean status;

}
