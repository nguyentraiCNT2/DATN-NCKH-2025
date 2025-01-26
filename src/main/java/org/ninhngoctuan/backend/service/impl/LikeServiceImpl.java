package org.ninhngoctuan.backend.service.impl;

import org.modelmapper.ModelMapper;
import org.ninhngoctuan.backend.context.RequestContext;
import org.ninhngoctuan.backend.dto.LikeDTO;
import org.ninhngoctuan.backend.entity.LikeEntity;
import org.ninhngoctuan.backend.entity.NotificationEntity;
import org.ninhngoctuan.backend.entity.PostEntity;
import org.ninhngoctuan.backend.entity.UserEntity;
import org.ninhngoctuan.backend.repository.LikeRepository;
import org.ninhngoctuan.backend.repository.NotificationRepository;
import org.ninhngoctuan.backend.repository.PostRepository;
import org.ninhngoctuan.backend.repository.UserRepository;
import org.ninhngoctuan.backend.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public List<LikeDTO> getLikesByPost(Long post_id) {
        try {
            List<LikeDTO> likeDTOS = new ArrayList<>();
            PostEntity post = postRepository.findByPostId(post_id).orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết"));
            List<LikeEntity> likes = likeRepository.findByPost(post);
            for (LikeEntity like : likes) {
                likeDTOS.add(modelMapper.map(like, LikeDTO.class));
            }
            return likeDTOS;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public LikeDTO likePost(Long post_id) {
        try {
            PostEntity post = postRepository.findByPostId(post_id).orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết"));
            String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
            List<LikeEntity> likes = likeRepository.findByPost(post);
            for (LikeEntity like : likes) {
                if (like.getUser().getUserId()==user.getUserId()) {
                    likeRepository.delete(like);
                    if (post.getTotalLike()<=0L){
                        post.setTotalLike(0L);

                    }else {
                        post.setTotalLike(post.getTotalLike() - 1L);
                    }
                    postRepository.save(post);
                    return null;
                }
            }
            LikeEntity like = new LikeEntity();
            like.setPost(post);
            like.setUser(user);
            like.setCreatedAt(new Date());
            LikeEntity savedLike = likeRepository.save(like);
            post.setTotalLike(post.getTotalLike() + 1L);
            postRepository.save(post);
            NotificationEntity notificationEntity = new NotificationEntity();
            String content = user.getFullName()+" đã thich bài viết "+post.getContent()+" của bạn";
            notificationEntity.setContent(content);
            notificationEntity.setUser(user);
            notificationEntity.setCreatedAt(new Date());
            notificationEntity.setType("LIKE");
            notificationEntity.setReadStatus(false);
            notificationRepository.save(notificationEntity);
            return modelMapper.map(savedLike, LikeDTO.class);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void cancelLike(Long post_id, Long like_id) {
        try {
            PostEntity post = postRepository.findByPostId(post_id).orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết"));
            List<LikeEntity> likes = likeRepository.findByPost(post);
            for (LikeEntity like : likes) {
                if (like.getLikeId()==like_id) {
                    likeRepository.delete(like);
                }
            }
            post.setTotalLike(post.getTotalLike() - 1);
            postRepository.save(post);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public boolean checkLike(Long post_id) {
        PostEntity post = postRepository.findByPostId(post_id).orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết"));
        List<LikeEntity> likes = likeRepository.findByPost(post);
        RequestContext context = RequestContext.get();
        for (LikeEntity like : likes) {
            if (like.getUser().getUserId()==context.getUserId()) {
                return true;
            }
        }
        return false;
    }
}
