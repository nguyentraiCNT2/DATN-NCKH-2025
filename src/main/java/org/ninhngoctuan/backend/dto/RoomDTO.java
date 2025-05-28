package org.ninhngoctuan.backend.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ninhngoctuan.backend.entity.ThemmeEntity;
import org.ninhngoctuan.backend.entity.UserEntity;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO {
    private  Long id;
    private UserDTO member1;
    private UserDTO member2;
    private String theme;
    private String themeCore;
    private ThemmeDTO thememeId;

}
