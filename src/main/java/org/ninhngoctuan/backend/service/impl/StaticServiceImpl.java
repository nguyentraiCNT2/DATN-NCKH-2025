package org.ninhngoctuan.backend.service.impl;

import org.ninhngoctuan.backend.repository.*;
import org.ninhngoctuan.backend.service.StaticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StaticServiceImpl implements StaticService {

    private UserRepository userRepository;
    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private LikeRepository likeRepository;
    private MessageRepository messageRepository;
    private ImagesRepository imagesRepository;
    private VideosRepository videosRepository;

    @Autowired
    public StaticServiceImpl(UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository, LikeRepository likeRepository, MessageRepository messageRepository, ImagesRepository imagesRepository, VideosRepository videosRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
        this.messageRepository = messageRepository;
        this.imagesRepository = imagesRepository;
        this.videosRepository = videosRepository;
    }

    @Override
    public int totalUser() {
        return (int) userRepository.count();
    }

    @Override
    public int totalPost() {
        return (int) postRepository.count();
    }

    @Override
    public int totalComment() {
        return (int) commentRepository.count();
    }

    @Override
    public int totalLike() {
        return (int) likeRepository.count();
    }

    @Override
    public int totalMessage() {
        return  (int) messageRepository.count();
    }

    @Override
    public int totalImage() {
        return (int) imagesRepository.count();
    }

    @Override
    public int totalVideo() {
        return (int) videosRepository.count();
    }
}

