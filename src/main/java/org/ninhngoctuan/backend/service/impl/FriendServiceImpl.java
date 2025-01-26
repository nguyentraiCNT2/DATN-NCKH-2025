package org.ninhngoctuan.backend.service.impl;

import org.modelmapper.ModelMapper;
import org.ninhngoctuan.backend.context.RequestContext;
import org.ninhngoctuan.backend.dto.FriendDTO;
import org.ninhngoctuan.backend.entity.FriendEntity;
import org.ninhngoctuan.backend.entity.NotificationEntity;
import org.ninhngoctuan.backend.entity.UserEntity;
import org.ninhngoctuan.backend.repository.FriendsRepository;
import org.ninhngoctuan.backend.repository.NotificationRepository;
import org.ninhngoctuan.backend.repository.UserRepository;
import org.ninhngoctuan.backend.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {

    private UserRepository userRepository;

    private FriendsRepository friendsRepository;

    private ModelMapper modelMapper;

    private NotificationRepository notificationRepository;

    public FriendServiceImpl(UserRepository userRepository, FriendsRepository friendsRepository, ModelMapper modelMapper, NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.friendsRepository = friendsRepository;
        this.modelMapper = modelMapper;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<FriendDTO> getFriends() {
        List<FriendDTO> list = new ArrayList<>();
        String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
        List<FriendEntity> friendEntities = friendsRepository.findByUserAndStatus(user,"active");
        if (friendEntities == null)
            throw  new RuntimeException("Không có bạn bè nào");
        for (FriendEntity friendEntity : friendEntities) {
            FriendDTO friendDTO = modelMapper.map(friendEntity, FriendDTO.class);
            list.add(friendDTO);
        }

        return list;
    }

    @Override
    public List<FriendDTO> getFollower() {
        List<FriendDTO> list = new ArrayList<>();
        String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
        List<FriendEntity> friendEntities = friendsRepository.findByFriendAndStatus(user,"active");
        if (friendEntities == null)
            throw  new RuntimeException("Không có bạn bè nào");
        for (FriendEntity friendEntity : friendEntities) {
            FriendDTO friendDTO = modelMapper.map(friendEntity, FriendDTO.class);
            list.add(friendDTO);
        }

        return list;
    }

    @Override
    public List<FriendDTO> getByStatus() {
        List<FriendDTO> list = new ArrayList<>();
        String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
        List<FriendEntity> friendEntities = friendsRepository.findByUserAndStatus(user,"block");
        if (friendEntities == null)
            throw  new RuntimeException("Không có bạn bè nào");
        for (FriendEntity friendEntity : friendEntities) {
            FriendDTO friendDTO = modelMapper.map(friendEntity, FriendDTO.class);
            list.add(friendDTO);
        }
        return list;
    }

    @Override
    public FriendDTO addFriend(FriendDTO friendDTO) {
        try {
            FriendEntity friendEntity = modelMapper.map(friendDTO, FriendEntity.class);
            String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
            UserEntity friend = userRepository.findByUserId(friendEntity.getFriend().getUserId()).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
            friendEntity.setUser(user);
            friendEntity.setFriend(friend);
            friendEntity.setCreatedAt(new Date());
            friendEntity.setStatus("active");
            friendsRepository.save(friendEntity);
            NotificationEntity notificationEntity = new NotificationEntity();
            String content = user.getFullName()+" đã theo giõi bạn";
            notificationEntity.setContent(content);
            notificationEntity.setUser(user);
            notificationEntity.setCreatedAt(new Date());
            notificationEntity.setType("FOLLOW");
            notificationEntity.setReadStatus(false);
            notificationRepository.save(notificationEntity);
            return modelMapper.map(friendEntity, FriendDTO.class);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public FriendDTO blockFriend(FriendDTO friendDTO) {
        String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
        UserEntity friend = userRepository.findByUserId(friendDTO.getFriend().getUserId()).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng 2"));
        FriendEntity friendEntities = friendsRepository.findByUserAndFriend(user,friend);
        friendEntities.setStatus("block");
        friendsRepository.save(friendEntities);
        return modelMapper.map(friendEntities, FriendDTO.class);
    }

    @Override
    public FriendDTO unblockFriend(FriendDTO friendDTO) {
        String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
        UserEntity friend = userRepository.findByUserId(friendDTO.getFriend().getUserId()).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        FriendEntity friendEntities = friendsRepository.findByUserAndFriend(user,friend);
        friendEntities.setStatus("active");
        friendsRepository.save(friendEntities);
        return modelMapper.map(friendEntities, FriendDTO.class);
    }

    @Override
    public void cancelFriend(FriendDTO friendDTO) {
        String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
        UserEntity friend = userRepository.findByUserId(friendDTO.getFriend().getUserId()).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        FriendEntity friendEntities = friendsRepository.findByUserAndFriend(user,friend);
        friendsRepository.delete(friendEntities);
    }

    @Override
    public boolean checkFriend(Long id) {
        String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
        UserEntity userEntity = userRepository.findByUserId(id).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng 2"));
        FriendEntity entity = friendsRepository.findByUserAndFriend(user,userEntity);
        if (entity != null )
            return true;
        else
            return false;
    }
}
