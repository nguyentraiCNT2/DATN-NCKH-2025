package org.ninhngoctuan.backend.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ninhngoctuan.backend.entity.UserEntity;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationDTO {
    private Long id ;
    private String name;
    private UserDTO firstPeople;
    private UserDTO secondPeople;
    private Timestamp createAt;
    private Timestamp updateAt;
    private boolean status;
}
