package org.ninhngoctuan.backend.service;

import org.ninhngoctuan.backend.Response.RelationshipRespone;
import org.ninhngoctuan.backend.dto.RelationDTO;
import org.ninhngoctuan.backend.dto.UserDTO;

import java.util.List;

public interface RelationService {
    List<RelationshipRespone> getByMe();
    List<RelationshipRespone> getAllById(Long id);
    void sendConnectionRequest (String name, Long id);
    void confirmConnection(Long id, Long userId);
    void rejectConnection(Long id);
    void deleteConnection(Long id);
List<UserDTO> getAllUser();
List<RelationshipRespone> getAllConnectionRequest();

}
