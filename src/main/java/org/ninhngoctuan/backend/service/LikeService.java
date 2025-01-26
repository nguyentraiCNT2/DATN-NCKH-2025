package org.ninhngoctuan.backend.service;

import org.ninhngoctuan.backend.dto.LikeDTO;

import java.util.List;

public interface LikeService {
    List<LikeDTO> getLikesByPost(Long post_id);
    boolean checkLike(Long post_id);
    LikeDTO likePost(Long post_id);
    void cancelLike(Long post_id, Long like_id);
}
