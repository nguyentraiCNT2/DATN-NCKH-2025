package org.ninhngoctuan.backend.service;

import org.ninhngoctuan.backend.dto.CommentDTO;

import java.util.List;

public interface CommentService {

    List<CommentDTO> getComments(Long postId);
    List<CommentDTO> getALL();
    CommentDTO createCommend(CommentDTO comment);
    CommentDTO updateCommend(CommentDTO comment);
    void deleteCommend(Long id);

}
