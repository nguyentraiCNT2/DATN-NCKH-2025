package org.ninhngoctuan.backend.service.impl;

import org.modelmapper.ModelMapper;
import org.ninhngoctuan.backend.context.RequestContext;
import org.ninhngoctuan.backend.dto.NotificationDTO;
import org.ninhngoctuan.backend.entity.FriendEntity;
import org.ninhngoctuan.backend.entity.NotificationEntity;
import org.ninhngoctuan.backend.entity.UserEntity;
import org.ninhngoctuan.backend.repository.FriendsRepository;
import org.ninhngoctuan.backend.repository.NotificationRepository;
import org.ninhngoctuan.backend.repository.UserRepository;
import org.ninhngoctuan.backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private NotificationRepository notificationRepository;
    private UserRepository userRepository;
    private FriendsRepository  friendsRepository;
    private ModelMapper modelMapper;
    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   UserRepository userRepository, FriendsRepository friendsRepository,
                                   ModelMapper modelMapper) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.friendsRepository = friendsRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<NotificationDTO> getNotificationsByFollow() {
        String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
        // Lấy danh sách bạn mà người dùng đang theo dõi
        List<FriendEntity> friendsAsUser = friendsRepository.findByUser(user);

        if (friendsAsUser.isEmpty()) throw new RuntimeException("Bạn chưa theo dõi ai.");

        List<NotificationDTO> notificationList = new ArrayList<>();

        friendsAsUser.stream()
                // Chỉ lấy các mối quan hệ bạn bè mà bạn đang theo dõi và không bị block
                .filter(friendEntity -> friendEntity.getStatus().equals("active"))
                .forEach(friendEntity -> {
                    UserEntity friend = friendEntity.getFriend();

                    // Kiểm tra xem bạn có bị block bởi người bạn này không
                    FriendEntity reciprocalFriendship = friendsRepository.findByUserAndFriend(friend, user);

                    // Nếu không có mối quan hệ đối ứng, coi như không bị block
                    boolean isNotBlocked = reciprocalFriendship == null || reciprocalFriendship.getStatus().equals("active");

                    // Chỉ lấy thông báo nếu không bị block
                    if (isNotBlocked) {
                        notificationRepository.findByUserOrderByCreatedAtDesc(friend)
                                .forEach(notificationEntity -> notificationList.add(modelMapper.map(notificationEntity, NotificationDTO.class)));
                    }
                });

        return notificationList;
    }


    @Override
    public NotificationDTO ReadNotification(Long id) {
        try{
            NotificationEntity notificationEntity = notificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy thông báo này "));
            notificationEntity.setReadStatus(true);

            NotificationEntity savenotification = notificationRepository.save(notificationEntity);
            return modelMapper.map(savenotification, NotificationDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void DeleteNotification(Long id) {

    }

    @Override
    public Long totalNotificationsNotRead() {
        String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
        // Lấy danh sách bạn mà người dùng đang theo dõi
        List<FriendEntity> friendsAsUser = friendsRepository.findByUser(user);

        if (friendsAsUser.isEmpty()) throw new RuntimeException("Bạn chưa theo dõi ai.");

        List<NotificationDTO> notificationList = new ArrayList<>();

        friendsAsUser.stream()
                // Chỉ lấy các mối quan hệ bạn bè mà bạn đang theo dõi và không bị block
                .filter(friendEntity -> friendEntity.getStatus().equals("active"))
                .forEach(friendEntity -> {
                    UserEntity friend = friendEntity.getFriend();

                    // Kiểm tra xem bạn có bị block bởi người bạn này không
                    FriendEntity reciprocalFriendship = friendsRepository.findByUserAndFriend(friend, user);

                    // Nếu không có mối quan hệ đối ứng, coi như không bị block
                    boolean isNotBlocked = reciprocalFriendship == null || reciprocalFriendship.getStatus().equals("active");

                    // Chỉ lấy thông báo nếu không bị block
                    if (isNotBlocked) {
                        notificationRepository.findByUserOrderByCreatedAtDesc(friend)
                                .stream().filter(notificationEntity -> notificationEntity.getReadStatus() == false)
                                .forEach(notificationEntity -> notificationList.add(modelMapper.map(notificationEntity, NotificationDTO.class)));
                    }
                });

        Long total = (long) notificationList.size();
        return total;
    }

    @Override
    public boolean readAllNotifications() {
        try {
            String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
            // Lấy danh sách bạn mà người dùng đang theo dõi
            List<FriendEntity> friendsAsUser = friendsRepository.findByUser(user);

            if (friendsAsUser.isEmpty()) throw new RuntimeException("Bạn chưa theo dõi ai.");

            List<NotificationDTO> notificationList = new ArrayList<>();

            friendsAsUser.stream()
                    // Chỉ lấy các mối quan hệ bạn bè mà bạn đang theo dõi và không bị block
                    .filter(friendEntity -> friendEntity.getStatus().equals("active"))
                    .forEach(friendEntity -> {
                        UserEntity friend = friendEntity.getFriend();

                        // Kiểm tra xem bạn có bị block bởi người bạn này không
                        FriendEntity reciprocalFriendship = friendsRepository.findByUserAndFriend(friend, user);

                        // Nếu không có mối quan hệ đối ứng, coi như không bị block
                        boolean isNotBlocked = reciprocalFriendship == null || reciprocalFriendship.getStatus().equals("active");

                        // Chỉ lấy thông báo nếu không bị block
                        if (isNotBlocked) {
                            notificationRepository.findByUserOrderByCreatedAtDesc(friend)
                                    .forEach(notificationEntity -> {
                                        notificationEntity.setReadStatus(true);
                                        notificationRepository.save(notificationEntity);
                                    } );
                        }
                    });
            return true;
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
