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
public class GroupPostDTO {
    private Long groupPostId;

    private GroupDTO group;

    private UserDTO user;

    private String content;

    private String imageUrl;

    private String videoUrl;

    private Date createdAt;

    private Date updatedAt;

    private Boolean isActive;


}
