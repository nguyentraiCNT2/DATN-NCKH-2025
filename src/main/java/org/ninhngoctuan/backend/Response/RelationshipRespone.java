package org.ninhngoctuan.backend.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ninhngoctuan.backend.dto.UserDTO;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationshipRespone {
    private Long id;
    private String name;
    private UserDTO userDTO;
    private Timestamp createAt;
    private Timestamp updateAt;
    private boolean status;
}
