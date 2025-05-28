package org.ninhngoctuan.backend.service.impl;

import org.modelmapper.ModelMapper;
import org.ninhngoctuan.backend.context.RequestContext;
import org.ninhngoctuan.backend.dto.*;
import org.ninhngoctuan.backend.entity.*;
import org.ninhngoctuan.backend.repository.*;
import org.ninhngoctuan.backend.service.FirebaseStorageService;
import org.ninhngoctuan.backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MessageServiceImpl implements MessageService {
    @Value("${images.dir}")
    private String imagesDir;
    @Value("${videos.dir}")
    private String videosDir;
    @Value("${audios.dir}")
    private String audiosDir;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private FriendsRepository friendsRepository;
    @Autowired
    private MessageVideosRepository messageVideosRepository;
    @Autowired
    private MessageImagesRepository messageImagesRepository;
    @Autowired
    private VideosRepository videosRepository;
    @Autowired
    private ImagesRepository imagesRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private FirebaseStorageService firebaseStorageService;
    @Autowired
    private AudiosRepository audiosRepository;
    @Autowired
    private MessageAudiosRepository messageAudiosRepository;

    @Override
    public MessageDTO sendMessage(MessageDTO message, List<MultipartFile> images, List<MultipartFile> videos) {
        try {

            MessageEntity messageEntity = modelMapper.map(message, MessageEntity.class);

            String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity sender = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
            UserEntity receiver = userRepository.findByUserId(messageEntity.getReceiver().getUserId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người nhận"));

            List<FriendEntity> friend1 = friendsRepository.findByUser(sender);
            for (FriendEntity friend : friend1) {
                if (friend.getFriend().getUserId().equals(receiver.getUserId()) && friend.getStatus().equalsIgnoreCase("block")) {
                    throw new RuntimeException("Bạn đã chặn người này, hãy bỏ chặn để có thể nhắn tin");
                }
            }
            List<FriendEntity> friend31 = friendsRepository.findByUser(receiver);
            for (FriendEntity friend : friend31) {
                if (friend.getFriend().getUserId().equals(sender.getUserId()) && friend.getStatus().equalsIgnoreCase("block")) {
                    throw new RuntimeException("Bạn đã bị chặn không thể nhắn tin");
                }
            }

            List<FriendEntity> friend2 = friendsRepository.findByFriend(receiver);
            for (FriendEntity friend : friend2) {
                if (friend.getUser().getUserId().equals(sender.getUserId()) && friend.getStatus().equalsIgnoreCase("block")) {
                    throw new RuntimeException("Bạn đã bị chặn không thể nhắn tin");
                }
            }
            List<FriendEntity> friendEntities = friendsRepository.findByFriend(sender);
            for (FriendEntity friend : friendEntities) {
                if (friend.getUser().getUserId().equals(receiver.getUserId()) && friend.getStatus().equalsIgnoreCase("block")) {
                    throw new RuntimeException("Bạn đã chặn người này, hãy bỏ chặn để có thể nhắn tin");
                }
            }

            RoomEntity room = roomRepository.findByMember1AndMember2OrMember1AndMember2(sender, receiver, receiver, sender)
                    .stream()
                    .filter(r -> (r.getMember1().equals(sender) && r.getMember2().equals(receiver)) ||
                            (r.getMember1().equals(receiver) && r.getMember2().equals(sender)))
                    .findFirst()
                    .orElseGet(() -> {
                        // Tạo phòng mới nếu không tồn tại
                        RoomEntity newRoom = new RoomEntity();
                        newRoom.setMember1(sender);
                        newRoom.setMember2(receiver);
                        newRoom.setTheme("xanh");
                        newRoom.setThemeCore("blue");
                        return roomRepository.save(newRoom);
                    });

            messageEntity.setSender(sender);
            messageEntity.setReceiver(receiver);
            messageEntity.setRoom(room); // Liên kết tin nhắn với phòng
            messageEntity.setCreatedAt(new Date());
            MessageEntity savedMessage = messageRepository.save(messageEntity);
            NotificationEntity notificationEntity = new NotificationEntity();
            String content = "bạn có một tin nhắn mới";
            notificationEntity.setContent(content);
            notificationEntity.setUser(sender);
            notificationEntity.setCreatedAt(new Date());
            notificationEntity.setType("MESSAGE");
            notificationEntity.setReadStatus(false);
            notificationRepository.save(notificationEntity);
            if (images != null && !images.isEmpty()) {
                Path uploadPathImages = Paths.get(imagesDir);
                if (!Files.exists(uploadPathImages)) {
                    Files.createDirectories(uploadPathImages);
                }

                for (MultipartFile image : images) {
                    if (!image.isEmpty()) {
                        String imageFileName = firebaseStorageService.uploadFile(image);
                        ImagesEntity imagesEntity = new ImagesEntity();
                        imagesEntity.setUrl(imageFileName);
                        imagesEntity.setCreate_at(new Date());
                        imagesEntity.setUpdate_at(new Date());
                       ImagesEntity savedImage = imagesRepository.save(imagesEntity);

                        MessageImagesEntity messageImagesEntity = new MessageImagesEntity();
                        messageImagesEntity.setImagesEntity(savedImage);
                        messageImagesEntity.setMessage(savedMessage);
                        messageImagesRepository.save(messageImagesEntity);
                    }
                }
            }
            if (videos != null && !videos.isEmpty()) {
                Path uploadPathVideos = Paths.get(videosDir);
                if (!Files.exists(uploadPathVideos)) {
                    Files.createDirectories(uploadPathVideos);
                }

                for (MultipartFile video : videos) {
                    if (!video.isEmpty()) {
                        String videoFileName = firebaseStorageService.uploadFile(video);
                        VideosEntity videosEntity = new VideosEntity();
                        videosEntity.setUrl(videoFileName);
                        videosEntity.setCreate_at(new Date());
                        videosEntity.setUpdate_at(new Date());
                        VideosEntity savedVideo = videosRepository.save(videosEntity);
                        MessageVideosEntity messageVideosEntity = new MessageVideosEntity();
                        messageVideosEntity.setVideo(savedVideo);
                        messageVideosEntity.setMessage(savedMessage);
                        messageVideosRepository.save(messageVideosEntity);
                    }
                }
            }
            return modelMapper.map(savedMessage, MessageDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<MessageDTO> getMessagesBetweenUsers(Long roomid) {
        List<MessageDTO> list = new ArrayList<>();
        RoomEntity room = roomRepository.findById(roomid).orElseThrow(() -> new RuntimeException("Không tìm thấy phòng chat"));

        // Tìm tất cả tin nhắn giữa người gửi và người nhận
        List<MessageEntity> messageEntities = messageRepository.findByRoom(room);

        for (MessageEntity messageEntity : messageEntities) {
            MessageDTO messageDTO = modelMapper.map(messageEntity, MessageDTO.class);
            list.add(messageDTO);
        }
        return list;
    }

    @Override
    public List<RoomDTO> getRoomsBetweenUsers() {
        List<RoomDTO> list = new ArrayList<>();
        String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
        List<RoomEntity> roomEntities = roomRepository.findByMember1OrMember2(user, user);

        for (RoomEntity room : roomEntities) {
            RoomDTO dto = modelMapper.map(room, RoomDTO.class);
            list.add(dto);
        }
        return list;
    }

    @Override
    public List<MessageImagesDTO> getImagesBetweenMessages(Long id) {

        List<MessageImagesDTO> list = new ArrayList<>();
        MessageEntity entity = messageRepository.findByMessageId(id)
                .orElseThrow(() -> new RuntimeException("Không có tin nhắn này "));
        List<MessageImagesEntity> messageImagesEntity = messageImagesRepository.findByMessage(entity);
            for (MessageImagesEntity messageImageEntity : messageImagesEntity) {
                MessageImagesDTO dto = modelMapper.map(messageImageEntity, MessageImagesDTO.class);
                list.add(dto);
            }
        return list;
    }

    @Override
    public List<MessageVideosDTO> getVideoBetweenMessages(Long id) {
        List<MessageVideosDTO> list = new ArrayList<>();
        MessageEntity entity = messageRepository.findByMessageId(id)
                .orElseThrow(() -> new RuntimeException("Không có tin nhắn này "));
        List<MessageVideosEntity> messageVideosEntities = messageVideosRepository.findByMessage(entity);
        for (MessageVideosEntity messageVideosEntity : messageVideosEntities) {
            list.add(modelMapper.map(messageVideosEntity, MessageVideosDTO.class));
        }
        return list;
    }

    @Override
    public List<MessageAudiosDTO> getAudioBetweenMessages(Long id) {
        List<MessageAudiosDTO> list = new ArrayList<>();
        MessageEntity entity = messageRepository.findByMessageId(id)
                .orElseThrow(() -> new RuntimeException("Không có tin nhắn này "));
        List<MessageAudiosEntity> messageAudiosEntities = messageAudiosRepository.findByMessageId(entity.getMessageId());
        for (MessageAudiosEntity messageAudiosEntity : messageAudiosEntities) {
            list.add(modelMapper.map(messageAudiosEntity, MessageAudiosDTO.class));
        }
        return list;
    }

    @Override
    public RoomDTO getRoomFriend(Long id) {
        try {
            String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity sender = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
            UserEntity receiver = userRepository.findByUserId(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người nhận"));

            RoomEntity room = roomRepository.findByMember1AndMember2OrMember1AndMember2(sender, receiver, receiver, sender)
                    .stream()
                    .filter(r -> (r.getMember1().equals(sender) && r.getMember2().equals(receiver)) ||
                            (r.getMember1().equals(receiver) && r.getMember2().equals(sender)))
                    .findFirst()
                    .orElseGet(() -> {
                        RoomEntity newRoom = new RoomEntity();
                        newRoom.setMember1(sender);
                        newRoom.setMember2(receiver);
                        newRoom.setTheme("xanh");
                        newRoom.setThemeCore("blue");
                        return roomRepository.save(newRoom);
                    });
            return modelMapper.map(room, RoomDTO.class);
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public MessageDTO sendAudio(Long receiverId, MultipartFile audio) {
        try {
            String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity sender = userRepository.findByEmail(authEmail)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
            UserEntity receiver = userRepository.findByUserId(receiverId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người nhận"));

            // Kiểm tra trạng thái chặn
            List<FriendEntity> friend1 = friendsRepository.findByUser(sender);
            for (FriendEntity friend : friend1) {
                if (friend.getFriend().getUserId().equals(receiver.getUserId()) && friend.getStatus().equalsIgnoreCase("block")) {
                    throw new RuntimeException("Bạn đã chặn người này, hãy bỏ chặn để có thể nhắn tin");
                }
            }
            List<FriendEntity> friend31 = friendsRepository.findByUser(receiver);
            for (FriendEntity friend : friend31) {
                if (friend.getFriend().getUserId().equals(sender.getUserId()) && friend.getStatus().equalsIgnoreCase("block")) {
                    throw new RuntimeException("Bạn đã bị chặn không thể nhắn tin");
                }
            }
            List<FriendEntity> friend2 = friendsRepository.findByFriend(receiver);
            for (FriendEntity friend : friend2) {
                if (friend.getUser().getUserId().equals(sender.getUserId()) && friend.getStatus().equalsIgnoreCase("block")) {
                    throw new RuntimeException("Bạn đã bị chặn không thể nhắn tin");
                }
            }
            List<FriendEntity> friendEntities = friendsRepository.findByFriend(sender);
            for (FriendEntity friend : friendEntities) {
                if (friend.getUser().getUserId().equals(receiver.getUserId()) && friend.getStatus().equalsIgnoreCase("block")) {
                    throw new RuntimeException("Bạn đã chặn người này, hãy bỏ chặn để có thể nhắn tin");
                }
            }
            // Tìm hoặc tạo phòng chat
            RoomEntity room = roomRepository.findByMember1AndMember2OrMember1AndMember2(sender, receiver, receiver, sender)
                    .stream()
                    .filter(r -> (r.getMember1().equals(sender) && r.getMember2().equals(receiver)) ||
                            (r.getMember1().equals(receiver) && r.getMember2().equals(sender)))
                    .findFirst()
                    .orElseGet(() -> {
                        RoomEntity newRoom = new RoomEntity();
                        newRoom.setMember1(sender);
                        newRoom.setMember2(receiver);
                        newRoom.setTheme("xanh");
                        newRoom.setThemeCore("blue");
                        return roomRepository.save(newRoom);
                    });

            // Tạo tin nhắn mới (không có nội dung văn bản)
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setSender(sender);
            messageEntity.setReceiver(receiver);
            messageEntity.setRoom(room);
            messageEntity.setCreatedAt(new Date());
            MessageEntity savedMessage = messageRepository.save(messageEntity);

            // Tạo thông báo
            NotificationEntity notificationEntity = new NotificationEntity();
            notificationEntity.setContent("Bạn có một tin nhắn âm thanh mới");
            notificationEntity.setUser(sender);
            notificationEntity.setCreatedAt(new Date());
            notificationEntity.setType("MESSAGE");
            notificationEntity.setReadStatus(false);
            notificationRepository.save(notificationEntity);

            // Xử lý file âm thanh
            if (audio != null && !audio.isEmpty()) {
                Path uploadPathAudios = Paths.get(audiosDir);
                if (!Files.exists(uploadPathAudios)) {
                    Files.createDirectories(uploadPathAudios);
                }
                String audioFileName = firebaseStorageService.uploadFile(audio);
                AudiosEntity audiosEntity = new AudiosEntity();
                audiosEntity.setUrl(audioFileName);
                audiosEntity.setCreateAt(new Date());
                audiosEntity.setUpdateAt( new Date());
                AudiosEntity savedAudio = audiosRepository.save(audiosEntity);

                MessageAudiosEntity messageAudiosEntity = new MessageAudiosEntity();
                messageAudiosEntity.setAudio(savedAudio);
                messageAudiosEntity.setMessage(savedMessage);
                messageAudiosRepository.save(messageAudiosEntity);
            } else {
                throw new RuntimeException("Không có file âm thanh được gửi");
            }

            return modelMapper.map(savedMessage, MessageDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
