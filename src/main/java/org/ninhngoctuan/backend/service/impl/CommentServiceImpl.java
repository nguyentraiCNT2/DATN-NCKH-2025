package org.ninhngoctuan.backend.service.impl;

import org.apache.coyote.Request;
import org.modelmapper.ModelMapper;
import org.ninhngoctuan.backend.context.RequestContext;
import org.ninhngoctuan.backend.dto.CommentDTO;
import org.ninhngoctuan.backend.entity.CommentEntity;
import org.ninhngoctuan.backend.entity.NotificationEntity;
import org.ninhngoctuan.backend.entity.PostEntity;
import org.ninhngoctuan.backend.entity.UserEntity;
import org.ninhngoctuan.backend.repository.CommentRepository;
import org.ninhngoctuan.backend.repository.NotificationRepository;
import org.ninhngoctuan.backend.repository.PostRepository;
import org.ninhngoctuan.backend.repository.UserRepository;
import org.ninhngoctuan.backend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private PostRepository postRepository;
    private ModelMapper modelMapper;
    private NotificationRepository notificationRepository;

    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository, ModelMapper modelMapper, NotificationRepository notificationRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<CommentDTO> getComments(Long postId) {
        PostEntity post = postRepository.findByPostId(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        List<CommentEntity> comments = commentRepository.findByPost(post);
        List<CommentDTO> commentDTOS = new ArrayList<>();
        for (CommentEntity commentEntity : comments) {
            commentDTOS.add(modelMapper.map(commentEntity, CommentDTO.class));
        }
        return commentDTOS;
    }

    @Override
    public List<CommentDTO> getALL() {
        List<CommentEntity> comments = commentRepository.findAll();
        List<CommentDTO> commentDTOS = new ArrayList<>();
        comments.stream().forEach(comment -> commentDTOS.add(modelMapper.map(comment, CommentDTO.class)));
        return commentDTOS;
    }

    @Override
    public CommentDTO createCommend(CommentDTO comment) {
        try {
            String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            CommentEntity commentEntity = modelMapper.map(comment, CommentEntity.class);
            PostEntity post = postRepository.findByPostId(commentEntity.getPost().getPostId()).orElseThrow(() -> new RuntimeException("Post not found"));
            UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("User not found"));
            commentEntity.setCreatedAt(new Date());
            commentEntity.setUser(user);
            commentEntity.setPost(post);
            CommentEntity savedComment = commentRepository.save(commentEntity);

            NotificationEntity notificationEntity = new NotificationEntity();
            String content = user.getFullName()+" đã bình luận trong bài viết "+post.getContent()+" của bạn";
            notificationEntity.setContent(content);
            notificationEntity.setUser(user);
            notificationEntity.setCreatedAt(new Date());
            notificationEntity.setType("Post");
            notificationEntity.setReadStatus(false);
            notificationRepository.save(notificationEntity);

            return modelMapper.map(savedComment, CommentDTO.class);
        }catch (Exception e) {
           throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public CommentDTO updateCommend(CommentDTO comment) {
        try {
            CommentEntity entity = commentRepository.findByCommentId(comment.getCommentId()).orElseThrow(() -> new RuntimeException("Comment not found"));
            entity.setContent(comment.getContent());
            entity.setCreatedAt(new Date());
            CommentEntity updatedComment = commentRepository.save(entity);
            return modelMapper.map(updatedComment, CommentDTO.class);
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteCommend(Long id) {
        try {
            CommentEntity entity = commentRepository.findByCommentId(id).orElseThrow(() -> new RuntimeException("Comment not found"));
                commentRepository.delete(entity);
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
