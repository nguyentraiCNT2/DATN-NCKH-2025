package org.ninhngoctuan.backend.service;

import org.ninhngoctuan.backend.dto.PostDTO;
import org.ninhngoctuan.backend.dto.PostImagesDTO;
import org.ninhngoctuan.backend.dto.PostVideoDTO;
import org.ninhngoctuan.backend.dto.TagDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

public interface PostService {
    PostDTO createPost(PostDTO  postDTO, List<MultipartFile> images, List<MultipartFile> videos);
    PostDTO updatePost(PostDTO  postDTO, List<MultipartFile> images, List<MultipartFile> videos);
    PostDTO deletePost(Long id);
    PostDTO showPost(Long id);
    Path getByFilename(String filename);
    List<PostDTO> getAllPostADMIN();
    Page<PostDTO> getAllPostDesc(Pageable pageable);
    List<PostDTO> getAllPostByGroupId(Long groupId);
    List<PostDTO> getAllPostByGroups();
    List<PostDTO> getByUserid();
    List<PostDTO> getByUserid(Long id);
    Page<PostDTO> getPostByFriend(Pageable pageable);
    List<PostImagesDTO> getPostImages(Long id);
    List<PostVideoDTO> getPostVideos(Long id);
    PostDTO getById(Long id);
    List<PostDTO> getAllOrderByTotalLikeDesc();
    List<PostDTO> getAllOrderByTotalCommentDesc();
}
