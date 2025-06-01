package org.ninhngoctuan.backend.service.impl;

import org.modelmapper.ModelMapper;
import org.ninhngoctuan.backend.Response.RelationshipRespone;
import org.ninhngoctuan.backend.dto.RelationDTO;
import org.ninhngoctuan.backend.dto.UserDTO;
import org.ninhngoctuan.backend.entity.FriendEntity;
import org.ninhngoctuan.backend.entity.NotificationEntity;
import org.ninhngoctuan.backend.entity.RelationEntity;
import org.ninhngoctuan.backend.entity.UserEntity;
import org.ninhngoctuan.backend.repository.FriendsRepository;
import org.ninhngoctuan.backend.repository.NotificationRepository;
import org.ninhngoctuan.backend.repository.RelationRepository;
import org.ninhngoctuan.backend.repository.UserRepository;
import org.ninhngoctuan.backend.service.RelationService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RelationServiceIMPL implements RelationService {
    private final UserRepository userRepository;
    private final RelationRepository relationRepository;
    private final ModelMapper modelMapper;
    private final NotificationRepository notificationRepository;
    private final FriendsRepository friendsRepository;

    public RelationServiceIMPL(UserRepository userRepository, RelationRepository relationRepository, ModelMapper modelMapper, NotificationRepository notificationRepository, FriendsRepository friendsRepository) {
        this.userRepository = userRepository;
        this.relationRepository = relationRepository;
        this.modelMapper = modelMapper;
        this.notificationRepository = notificationRepository;
        this.friendsRepository = friendsRepository;

    }

    @Override
    public List<RelationshipRespone> getByMe() {
        try {
            List<RelationshipRespone> relationDTOList = new ArrayList<>();
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("Bạn chưa đăng nhập vui lòng đăng nhập để sử dụng chức năng"));
            List<RelationEntity> relationEntities = relationRepository.findByFirstPeopleIdOrSecondPeopleId(user.getUserId());
            relationEntities.stream()
                    .filter(relationEntity -> relationEntity.isStatus())
                    .forEach(relationEntity -> {
                        RelationshipRespone relationshipRespone = new RelationshipRespone();
                        relationshipRespone.setId(relationEntity.getId());
                        relationshipRespone.setName(relationEntity.getName());

                        if (!relationEntity.getFirstPeople().getUserId().equals(user.getUserId())){
                            relationshipRespone.setUserDTO(modelMapper.map(relationEntity.getFirstPeople(), UserDTO.class));
                        }else {
                            relationshipRespone.setUserDTO(modelMapper.map(relationEntity.getSecondPeople(), UserDTO.class));
                        }
                        relationshipRespone.setCreateAt(relationEntity.getCreateAt());
                        relationshipRespone.setUpdateAt(relationEntity.getUpdateAt());
                        relationshipRespone.setStatus(relationEntity.isStatus());
                        relationDTOList.add(relationshipRespone);
                    });
            return relationDTOList;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public List<RelationshipRespone> getAllById(Long id) {
        try {
            List<RelationshipRespone> relationDTOList = new ArrayList<>();
            UserEntity user = userRepository.findByUserId(id).orElseThrow(() -> new RuntimeException("Bạn chưa đăng nhập vui lòng đăng nhập để sử dụng chức năng"));
            List<RelationEntity> relationEntities = relationRepository.findByFirstPeopleIdOrSecondPeopleId(user.getUserId());
            relationEntities.stream()
                    .filter(relationEntity -> relationEntity.isStatus())
                    .forEach(relationEntity -> {
                        RelationshipRespone relationshipRespone = new RelationshipRespone();
                        relationshipRespone.setId(relationEntity.getId());
                        relationshipRespone.setName(relationEntity.getName());

                        if (!relationEntity.getFirstPeople().getUserId().equals(user.getUserId())){
                            relationshipRespone.setUserDTO(modelMapper.map(relationEntity.getFirstPeople(), UserDTO.class));
                        }else {
                            relationshipRespone.setUserDTO(modelMapper.map(relationEntity.getSecondPeople(), UserDTO.class));
                        }
                        relationshipRespone.setCreateAt(relationEntity.getCreateAt());
                        relationshipRespone.setUpdateAt(relationEntity.getUpdateAt());
                        relationshipRespone.setStatus(relationEntity.isStatus());
                        relationDTOList.add(relationshipRespone);
                    });
            return relationDTOList;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void sendConnectionRequest(String name, Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity firstPeople= userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("Bạn chưa đăng nhập vui lòng đăng nhập để sử dụng chức năng"));
        UserEntity secondPeople = userRepository.findByUserId(id).orElseThrow(() -> new RuntimeException("Không tìm tháy ngời dùng này"));

        List<FriendEntity> friend1 = friendsRepository.findByUser(firstPeople);
        for (FriendEntity friend : friend1) {
            if (friend.getFriend().getUserId().equals(secondPeople.getUserId()) && friend.getStatus().equalsIgnoreCase("block")) {
                throw new RuntimeException("Bạn đã chặn người này, hãy bỏ chặn để có thể kết nối");
            }
        }
        List<FriendEntity> friend31 = friendsRepository.findByUser(secondPeople);
        for (FriendEntity friend : friend31) {
            if (friend.getFriend().getUserId().equals(firstPeople.getUserId()) && friend.getStatus().equalsIgnoreCase("block")) {
                throw new RuntimeException("Bạn đã bị chặn không thể kết nối");
            }
        }

        List<FriendEntity> friend2 = friendsRepository.findByFriend(secondPeople);
        for (FriendEntity friend : friend2) {
            if (friend.getUser().getUserId().equals(firstPeople.getUserId()) && friend.getStatus().equalsIgnoreCase("block")) {
                throw new RuntimeException("Bạn đã bị chặn không thể kêt nối");
            }
        }
        List<FriendEntity> friendEntities = friendsRepository.findByFriend(firstPeople);
        for (FriendEntity friend : friendEntities) {
            if (friend.getUser().getUserId().equals(secondPeople.getUserId()) && friend.getStatus().equalsIgnoreCase("block")) {
                throw new RuntimeException("Bạn đã chặn người này, hãy bỏ chặn để có thể kết nối");
            }
        }
        RelationEntity relationEntity = new RelationEntity();
        relationEntity.setFirstPeople(firstPeople);
        relationEntity.setSecondPeople(secondPeople);
        relationEntity.setName(name);
        relationEntity.setCreateAt(Timestamp.from(Instant.now()));
        relationEntity.setStatus(false);
        relationRepository.save(relationEntity);
    }

    @Override
    public void confirmConnection(Long id, Long userId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity firstPeople= userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("Bạn chưa đăng nhập vui lòng đăng nhập để sử dụng chức năng"));
        UserEntity secondPeople = userRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Không tìm tháy ngời dùng này"));

        List<FriendEntity> friend1 = friendsRepository.findByUser(firstPeople);
        for (FriendEntity friend : friend1) {
            if (friend.getFriend().getUserId().equals(secondPeople.getUserId()) && friend.getStatus().equalsIgnoreCase("block")) {
                throw new RuntimeException("Bạn đã chặn người này, hãy bỏ chặn để có thể kết nối");
            }
        }
        List<FriendEntity> friend31 = friendsRepository.findByUser(secondPeople);
        for (FriendEntity friend : friend31) {
            if (friend.getFriend().getUserId().equals(firstPeople.getUserId()) && friend.getStatus().equalsIgnoreCase("block")) {
                throw new RuntimeException("Bạn đã bị chặn không thể kết nối");
            }
        }

        List<FriendEntity> friend2 = friendsRepository.findByFriend(secondPeople);
        for (FriendEntity friend : friend2) {
            if (friend.getUser().getUserId().equals(firstPeople.getUserId()) && friend.getStatus().equalsIgnoreCase("block")) {
                throw new RuntimeException("Bạn đã bị chặn không thể kêt nối");
            }
        }
        List<FriendEntity> friendEntities = friendsRepository.findByFriend(firstPeople);
        for (FriendEntity friend : friendEntities) {
            if (friend.getUser().getUserId().equals(secondPeople.getUserId()) && friend.getStatus().equalsIgnoreCase("block")) {
                throw new RuntimeException("Bạn đã chặn người này, hãy bỏ chặn để có thể kết nối");
            }
        }

        RelationEntity relationEntity = relationRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy kết nối này"));
        relationEntity.setStatus(true);
        relationEntity.setUpdateAt(Timestamp.from(Instant.now()));
        relationRepository.save(relationEntity);
    }

    @Override
    public void rejectConnection(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity firstPeople= userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("Bạn chưa đăng nhập vui lòng đăng nhập để sử dụng chức năng"));

        RelationEntity relationEntity = relationRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy kết nối này"));
        relationRepository.delete(relationEntity);
    }

    @Override
    public void deleteConnection(Long id) {
        RelationEntity relationEntity = relationRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy kết nối này"));
        relationRepository.delete(relationEntity);
    }

    @Override
    public List<UserDTO> getAllUser() {
        try {
            List<UserDTO> userDTOList = new ArrayList<>();

            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("Bạn chưa đăng nhập vui lòng đăng nhập để sử dụng chức năng"));
            // Lấy danh sách bạn bè mà người dùng đang theo dõi
            List<FriendEntity> followingFriends = friendsRepository.findByUser(user).stream()
                    .filter(friend -> "active".equals(friend.getStatus()))
                    .collect(Collectors.toList());
            // Kiểm tra mối quan hệ hai chiều và thêm vào danh sách
            for (FriendEntity friendship : followingFriends) {
                UserEntity friend = friendship.getFriend();
                // Kiểm tra xem friend có theo dõi lại currentUser không
                boolean isMutualFriend = friendsRepository.existsByUserAndFriendAndStatus(friend, user, "active");
                if (isMutualFriend && !friend.getUserId().equals(user.getUserId())) {
                    userDTOList.add(modelMapper.map(friend, UserDTO.class));
                }
            }
             return userDTOList;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<RelationshipRespone> getAllConnectionRequest() {
        try {
            List<RelationshipRespone> relationDTOList = new ArrayList<>();
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("Bạn chưa đăng nhập vui lòng đăng nhập để sử dụng chức năng"));
            List<RelationEntity> relationEntities = relationRepository.findByConnectionRequest(user.getUserId());
            relationEntities.stream()
                    .filter(relationEntity -> !relationEntity.isStatus())
                    .forEach(relationEntity -> {
                        RelationshipRespone relationshipRespone = new RelationshipRespone();
                        relationshipRespone.setId(relationEntity.getId());
                        relationshipRespone.setName(relationEntity.getName());

                        if (!relationEntity.getFirstPeople().getUserId().equals(user.getUserId())){
                            relationshipRespone.setUserDTO(modelMapper.map(relationEntity.getFirstPeople(), UserDTO.class));
                        }else {
                            relationshipRespone.setUserDTO(modelMapper.map(relationEntity.getSecondPeople(), UserDTO.class));
                        }
                        relationshipRespone.setCreateAt(relationEntity.getCreateAt());
                        relationshipRespone.setUpdateAt(relationEntity.getUpdateAt());
                        relationshipRespone.setStatus(relationEntity.isStatus());
                        relationDTOList.add(relationshipRespone);
                    });
            return relationDTOList;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
