package org.ninhngoctuan.backend.service.impl;

import org.modelmapper.ModelMapper;
import org.ninhngoctuan.backend.context.RequestContext;
import org.ninhngoctuan.backend.dto.*;
import org.ninhngoctuan.backend.entity.*;
import org.ninhngoctuan.backend.repository.*;
import org.ninhngoctuan.backend.service.FirebaseStorageService;
import org.ninhngoctuan.backend.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Value("${images.dir}")
    private String imagesDir;
    @Value("${videos.dir}")
    private String videosDir;
    private ModelMapper modelMapper;
    private PostRepository postRepository;
    private UserRepository userRepository;
    private TagRepository tagRepository;
    private FriendsRepository friendsRepository;
    private ImagesRepository imagesRepository;
    private VideosRepository videosRepository;
    private PostImagesRepository postImagesRepository;
    private PostVideosRepository postVideosRepository;
    private NotificationRepository notificationRepository;
    private CommentRepository commentRepository;
    private FirebaseStorageService firebaseStorageService;
    private final GroupRepository groupRepository;
    private final  GroupMemberRepository groupMemberRepository;

    public PostServiceImpl(ModelMapper modelMapper, PostRepository postRepository, UserRepository userRepository, TagRepository tagRepository, FriendsRepository friendsRepository, ImagesRepository imagesRepository, VideosRepository videosRepository, PostImagesRepository postImagesRepository, PostVideosRepository postVideosRepository, NotificationRepository notificationRepository, CommentRepository commentRepository, FirebaseStorageService firebaseStorageService, GroupRepository groupRepository, GroupMemberRepository groupMemberRepository) {
        this.modelMapper = modelMapper;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.friendsRepository = friendsRepository;
        this.imagesRepository = imagesRepository;
        this.videosRepository = videosRepository;
        this.postImagesRepository = postImagesRepository;
        this.postVideosRepository = postVideosRepository;
        this.notificationRepository = notificationRepository;
        this.commentRepository = commentRepository;
        this.firebaseStorageService = firebaseStorageService;
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    @Override
    public List<PostDTO> getAllPostADMIN() {
        try {
            List<PostDTO> list = new ArrayList<>();
            List<PostEntity> postEntities = postRepository.findAll();
            postEntities.forEach(postEntity -> {
                PostDTO postDTO = modelMapper.map(postEntity, PostDTO.class);
                list.add(postDTO);
            });
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public PostDTO createPost(PostDTO postDTO, List<MultipartFile> images, List<MultipartFile> videos) {
        try {
            PostEntity post = modelMapper.map(postDTO, PostEntity.class);

            // Lấy thông tin người dùng từ UserId
            UserEntity user = userRepository.findByUserId(post.getUser().getUserId())
                    .orElseThrow(() -> new RuntimeException("Không có tài khoản nào có id là: " + post.getUser().getUserId()));

            // Kiểm tra và gán TagEntity nếu có
            if (post.getTagId() != null && post.getTagId().getTagId() != null) {
                TagEntity tagEntity = tagRepository.findById(post.getTagId().getTagId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy nhãn nào có id là: " + post.getTagId().getTagId()));
                post.setTagId(tagEntity);
            } else {
                post.setTagId(null);
            }
            // Kiểm tra và gán TagEntity nếu có
            if (post.getGroupId() != null && post.getGroupId().getGroupId() != null) {
                GroupEntity groupEntity = groupRepository.findByGroupId(post.getGroupId().getGroupId()).orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm này"));
                post.setGroupId(groupEntity);
            } else {
                post.setGroupId(null);
            }

            // Tạo thư mục lưu trữ hình ảnh và video nếu chưa có
            Path uploadPathImages = Paths.get(imagesDir);
            Path uploadPathVideos = Paths.get(videosDir);

            if (!Files.exists(uploadPathImages)) {
                Files.createDirectories(uploadPathImages);
            }
            if (!Files.exists(uploadPathVideos)) {
                Files.createDirectories(uploadPathVideos);
            }

            post.setCreatedAt(new java.util.Date());
            post.setUser(user);
            post.setTotalLike(0L);
            post.setImageUrl(null);
            post.setVideoUrl(null);


            // Lưu bài viết
            PostEntity savedPost = postRepository.save(post);

            // Tạo thông báo cho bài viết mới
            NotificationEntity notificationEntity = new NotificationEntity();
            String content = user.getFullName() + " đã đăng một bài viết mới.";
            notificationEntity.setContent(content);
            notificationEntity.setUser(user);
            notificationEntity.setCreatedAt(new Date());
            notificationEntity.setType("POST");
            notificationEntity.setReadStatus(false);
            notificationRepository.save(notificationEntity);

            // Xử lý hình ảnh
            if (images != null && !images.isEmpty()) {
                for (MultipartFile image : images) {
                    if (!image.isEmpty()) {
                        String imageFileName = firebaseStorageService.uploadFile(image);

                        ImagesEntity imagesEntity = new ImagesEntity();
                        imagesEntity.setUrl(imageFileName);
                        imagesEntity.setCreate_at(new Date());
                        imagesEntity.setUpdate_at(new Date());
                        ImagesEntity savedImage = imagesRepository.save(imagesEntity);

                        PostImagesEntity postImagesEntity = new PostImagesEntity();
                        postImagesEntity.setImages(savedImage);
                        postImagesEntity.setPost(savedPost);
                        postImagesRepository.save(postImagesEntity);
                    }
                }
            }

            // Xử lý video (chunking)
            if (videos != null && !videos.isEmpty()) {
                for (MultipartFile video : videos) {
                    if (!video.isEmpty()) {
                        // Tải video lên Firebase Storage và nhận URL
                        String videoFileUrl = firebaseStorageService.uploadFile(video);

                        // Lưu thông tin video vào cơ sở dữ liệu
                        VideosEntity videosEntity = new VideosEntity();
                        videosEntity.setUrl(videoFileUrl); // Sử dụng URL từ Firebase
                        videosEntity.setCreate_at(new Date());
                        videosEntity.setUpdate_at(new Date());
                        VideosEntity savedVideo = videosRepository.save(videosEntity);

                        // Liên kết video với bài viết
                        PostVideoEntity postVideoEntity = new PostVideoEntity();
                        postVideoEntity.setVideos(savedVideo);
                        postVideoEntity.setPost(savedPost);
                        postVideosRepository.save(postVideoEntity);
                    }
                }
            }
            return modelMapper.map(savedPost, PostDTO.class);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, List<MultipartFile> images, List<MultipartFile> videos) {
        try {
            // Chuyển đổi DTO thành Entity
            PostEntity post = postRepository.findByPostId(postDTO.getPostId()).orElseThrow(() -> new RuntimeException("Không tim thấy tài khoản có id là "+postDTO.getPostId()));
            post.setContent(postDTO.getContent());
            // Lấy thông tin người dùng từ UserId
            UserEntity user = userRepository.findByUserId(post.getUser().getUserId())
                    .orElseThrow(() -> new RuntimeException("Không có tài khoản nào có id là: " + post.getUser().getUserId()));

            // Kiểm tra và gán TagEntity nếu có
            if (post.getTagId() != null && post.getTagId().getTagId() != null) {
                TagEntity tagEntity = tagRepository.findById(post.getTagId().getTagId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy nhãn nào có id là: " + post.getTagId().getTagId()));
                post.setTagId(tagEntity);
            } else {
                post.setTagId(null);
            }
            // Kiểm tra và gán TagEntity nếu có
            if (post.getGroupId() != null && post.getGroupId().getGroupId() != null) {
                GroupEntity groupEntity = groupRepository.findByGroupId(post.getGroupId().getGroupId()).orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm này"));
                post.setGroupId(groupEntity);
            } else {
                post.setGroupId(null);
            }

            // Tạo thư mục lưu trữ hình ảnh và video nếu chưa có
            Path uploadPathImages = Paths.get(imagesDir);
            Path uploadPathVideos = Paths.get(videosDir);

            if (!Files.exists(uploadPathImages)) {
                Files.createDirectories(uploadPathImages);
            }
            if (!Files.exists(uploadPathVideos)) {
                Files.createDirectories(uploadPathVideos);
            }

            post.setCreatedAt(new java.util.Date());
            post.setUser(user);
            post.setTotalLike(0L);
            post.setImageUrl(null);
            post.setVideoUrl(null);

            // Lưu bài viết
            PostEntity savedPost = postRepository.save(post);

            // Tạo thông báo cho bài viết mới
            NotificationEntity notificationEntity = new NotificationEntity();
            String content = user.getFullName() + " đã đăng một bài viết mới.";
            notificationEntity.setContent(content);
            notificationEntity.setUser(user);
            notificationEntity.setCreatedAt(new Date());
            notificationEntity.setType("POST");
            notificationEntity.setReadStatus(false);
            notificationRepository.save(notificationEntity);

            // Xử lý hình ảnh
            if (images != null && !images.isEmpty()) {
                for (MultipartFile image : images) {
                    if (!image.isEmpty()) {
                        String imageFileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
                        Path imageFilePath = uploadPathImages.resolve(imageFileName);
                        Files.copy(image.getInputStream(), imageFilePath, StandardCopyOption.REPLACE_EXISTING);

                        ImagesEntity imagesEntity = new ImagesEntity();
                        imagesEntity.setUrl(imageFileName);
                        imagesEntity.setCreate_at(new Date());
                        imagesEntity.setUpdate_at(new Date());
                        ImagesEntity savedImage = imagesRepository.save(imagesEntity);

                        PostImagesEntity postImagesEntity = new PostImagesEntity();
                        postImagesEntity.setImages(savedImage);
                        postImagesEntity.setPost(savedPost);
                        postImagesRepository.save(postImagesEntity);
                    }
                }
            }

            // Xử lý video (chunking)
            if (videos != null && !videos.isEmpty()) {
                for (MultipartFile video : videos) {
                    if (!video.isEmpty()) {
                        String videoFileName = UUID.randomUUID().toString() + "_" + video.getOriginalFilename();
                        Path videoFilePath = uploadPathVideos.resolve(videoFileName);

                        // Đọc các phần video từ video multipart
                        long chunkSize = 1024 * 1024 * 5; // 5MB mỗi phần
                        try (InputStream inputStream = video.getInputStream();
                             OutputStream outputStream = Files.newOutputStream(videoFilePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {

                            byte[] buffer = new byte[(int) chunkSize];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                            }
                        }

                        // Lưu thông tin video vào cơ sở dữ liệu
                        VideosEntity videosEntity = new VideosEntity();
                        videosEntity.setUrl(videoFileName);
                        videosEntity.setCreate_at(new Date());
                        videosEntity.setUpdate_at(new Date());
                        VideosEntity savedVideo = videosRepository.save(videosEntity);

                        PostVideoEntity postVideoEntity = new PostVideoEntity();
                        postVideoEntity.setVideos(savedVideo);
                        postVideoEntity.setPost(savedPost);
                        postVideosRepository.save(postVideoEntity);
                    }
                }
            }

            return modelMapper.map(savedPost, PostDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("An error occurred while creating the post: " + e.getMessage(), e);
        }
    }

    @Override
    public PostDTO deletePost(Long id) {
        PostEntity post = postRepository.findByPostId(id).orElseThrow(() -> new RuntimeException("Không tim thấy bài viết"));
        post.setDeleted(true);
       PostEntity savepost = postRepository.save(post);
        return modelMapper.map(savepost, PostDTO.class);
    }

    @Override
    public PostDTO showPost(Long id) {
        PostEntity post = postRepository.findByPostId(id).orElseThrow(() -> new RuntimeException("Không tim thấy bài viết"));
        post.setDeleted(false);
        PostEntity savepost = postRepository.save(post);
        return modelMapper.map(savepost, PostDTO.class);
    }

    @Override
    public Path getByFilename(String filename) {
        return  Paths.get(imagesDir).resolve(filename);
    }

    @Override
    public List<PostDTO> getAllPostDesc() {
        String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));

        // Lấy danh sách toàn bộ bài viết (chưa bị xóa)
        List<PostEntity> allPosts = postRepository.findAllRandom();

        // Lấy danh sách bạn bè mà người dùng đang theo dõi
        List<FriendEntity> followingFriends = friendsRepository.findByUser(user).stream()
                .filter(friend -> "active".equals(friend.getStatus()))
                .collect(Collectors.toList());

        // Lọc danh sách bài viết dựa trên điều kiện
        return allPosts.stream()
                .filter(post -> {
                    UserEntity postOwner = post.getUser();

                    // Kiểm tra nếu bài viết thuộc về chính người dùng
                    if (postOwner.equals(user)) {
                        return true;
                    }

                    // Kiểm tra nếu người dùng đang theo dõi chủ bài viết
                    boolean isFollowing = followingFriends.stream()
                            .anyMatch(friend -> friend.getFriend().equals(postOwner));


                    // Kiểm tra mối quan hệ đối ứng (họ có theo dõi lại mình không)
                    FriendEntity reciprocalFriendship = friendsRepository.findByUserAndFriend(postOwner, user);
                    boolean isNotBlockedByMe = reciprocalFriendship == null || "active".equals(reciprocalFriendship.getStatus());

                    // Kiểm tra nếu người dùng không bị block hoặc không block người kia
                    return isNotBlockedByMe ;
                })
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDTO> getAllPostByGroupId(Long groupId) {
        String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));

        // Lấy danh sách toàn bộ bài viết (chưa bị xóa)
        List<PostEntity> allPosts = postRepository.findPostByGroupId(groupId);

        // Lấy danh sách bạn bè mà người dùng đang theo dõi
        List<FriendEntity> followingFriends = friendsRepository.findByUser(user).stream()
                .filter(friend -> "active".equals(friend.getStatus()))
                .collect(Collectors.toList());

        // Lọc danh sách bài viết dựa trên điều kiện
        return allPosts.stream()
                .filter(postEntity -> postEntity.getGroupId().isDeleted() != true)
                .filter(post -> {
                    UserEntity postOwner = post.getUser();

                    // Kiểm tra nếu bài viết thuộc về chính người dùng
                    if (postOwner.equals(user)) {
                        return true;
                    }
                    // Kiểm tra nếu người dùng đang theo dõi chủ bài viết
                    boolean isFollowing = followingFriends.stream()
                            .anyMatch(friend -> friend.getFriend().equals(postOwner));

                    // Kiểm tra mối quan hệ đối ứng (họ có theo dõi lại mình không)
                    FriendEntity reciprocalFriendship = friendsRepository.findByUserAndFriend(postOwner, user);
                    boolean isNotBlockedByMe = reciprocalFriendship == null || "active".equals(reciprocalFriendship.getStatus());

                    // Kiểm tra nếu người dùng không bị block hoặc không block người kia
                    return isNotBlockedByMe ;
                })
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDTO> getAllPostByGroups() {
        String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
        List<GroupEntity> groupEntities = new ArrayList<>();
        List<GroupMemberEntity> groupMemberEntities = groupMemberRepository.findByUser(user);
        groupMemberEntities.stream()
                .forEach(groupMemberEntity -> {
                    groupEntities.add(groupMemberEntity.getGroup());
                });
        // Lấy danh sách toàn bộ bài viết (chưa bị xóa)
        List<PostEntity> allPosts = new ArrayList<>();
        groupEntities.stream()
                .forEach(groupEntity -> {
                    allPosts.addAll(postRepository.findPostByGroupId(groupEntity.getGroupId()));
                });


        // Lấy danh sách bạn bè mà người dùng đang theo dõi
        List<FriendEntity> followingFriends = friendsRepository.findByUser(user).stream()
                .filter(friend -> "active".equals(friend.getStatus()))
                .collect(Collectors.toList());

        // Lọc danh sách bài viết dựa trên điều kiện
        return allPosts.stream()
                .filter(postEntity -> postEntity.getGroupId().isDeleted() != true)
                .filter(post -> {
                    UserEntity postOwner = post.getUser();

                    // Kiểm tra nếu bài viết thuộc về chính người dùng
                    if (postOwner.equals(user)) {
                        return true;
                    }
                    // Kiểm tra nếu người dùng đang theo dõi chủ bài viết
                    boolean isFollowing = followingFriends.stream()
                            .anyMatch(friend -> friend.getFriend().equals(postOwner));

                    // Kiểm tra mối quan hệ đối ứng (họ có theo dõi lại mình không)
                    FriendEntity reciprocalFriendship = friendsRepository.findByUserAndFriend(postOwner, user);
                    boolean isNotBlockedByMe = reciprocalFriendship == null || "active".equals(reciprocalFriendship.getStatus());

                    // Kiểm tra nếu người dùng không bị block hoặc không block người kia
                    return isNotBlockedByMe ;
                })
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDTO> getByUserid() {
        String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
        List<PostEntity> postEntities = postRepository.findByUserOrderByCreatedAtDesc(user);
        List<PostDTO> list = new ArrayList<>();
        postEntities.stream()
                .filter(postEntity -> !postEntity.isDeleted())
                .forEach(postEntity -> {
                    PostDTO postDTO = modelMapper.map(postEntity, PostDTO.class);
                    list.add(postDTO);
                });

        return list;
    }

    @Override
    public List<PostDTO> getPostByFriend() {
        String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));


        // Lấy danh sách bạn mà người dùng đang theo dõi
        List<FriendEntity> friendsAsUser = friendsRepository.findByUser(user);

        if (friendsAsUser.isEmpty()) throw new RuntimeException("Bạn chưa theo dõi ai.");

        List<PostDTO> postList = new ArrayList<>();

        friendsAsUser.stream()
                // Chỉ lấy các mối quan hệ bạn bè mà bạn đang theo dõi và không bị block
                .filter(friendEntity -> friendEntity.getStatus().equals("active"))
                .forEach(friendEntity -> {
                    UserEntity friend = friendEntity.getFriend();

                    // Kiểm tra xem bạn có bị block bởi người bạn này không
                    FriendEntity reciprocalFriendship = friendsRepository.findByUserAndFriend(friend, user);

                    // Nếu không có mối quan hệ đối ứng, coi như không bị block
                    boolean isNotBlocked = reciprocalFriendship == null || reciprocalFriendship.getStatus().equals("active");

                    // Chỉ lấy bài viết nếu không bị block
                    if (isNotBlocked) {
                        postRepository.findByUserOrderByCreatedAtDesc(friend).stream()
                                .forEach(postEntity -> {
                                    if (!postEntity.isDeleted()) {
                                        postList.add(modelMapper.map(postEntity, PostDTO.class)); // Thêm dấu ngoặc đơn ở đây
                                    }
                                });
                    }
                });

        return postList;
    }

    @Override
    public List<PostImagesDTO> getPostImages(Long id) {
        List<PostImagesDTO> postImages = new ArrayList<>();
        PostEntity dto = postRepository.findByPostId(id).orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết"));
        List<PostImagesEntity>postImagesEntities = postImagesRepository.findByPost(dto);
        postImagesEntities.stream().forEach(postImagesEntity -> postImages.add(modelMapper.map(postImagesEntity, PostImagesDTO.class)));
        return postImages;
    }

    @Override
    public List<PostVideoDTO> getPostVideos(Long id) {
        List<PostVideoDTO> postVideos = new ArrayList<>();
        PostEntity dto = postRepository.findByPostId(id).orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết"));
        List<PostVideoEntity>postVideoEntities = postVideosRepository.findByPost(dto);
        postVideoEntities.stream().forEach(postVideoEntity -> postVideos.add(modelMapper.map(postVideoEntity, PostVideoDTO.class)));
        return postVideos;
    }

    @Override
    public List<PostDTO> getByUserid(Long id) {
        UserEntity user = userRepository.findByUserId(id).orElseThrow(() -> new RuntimeException("Không tìm tháy người dùng"));
        List<PostDTO> list = new ArrayList<>();
        List<PostEntity> postEntities = postRepository.findByUserOrderByCreatedAtDesc(user);
        for (PostEntity post: postEntities){
            if (!post.isDeleted()){
                PostDTO postDTO = modelMapper.map(post, PostDTO.class);
                list.add(postDTO);
            }
        }
        return list;
    }

    @Override
    public PostDTO getById(Long id) {
        try {
            PostEntity post = postRepository.findByPostId(id).orElseThrow(() -> new RuntimeException("Không tìm thầy bài viết"));
            return modelMapper.map(post, PostDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<PostDTO> getAllOrderByTotalLikeDesc() {
        try {
            List<PostDTO> list = new ArrayList<>();
            List<PostEntity> postEntities = postRepository.findByAllOrderByTotalLikeDesc();
            postEntities.forEach(postEntity -> {
                PostDTO postDTO = modelMapper.map(postEntity, PostDTO.class);
                list.add(postDTO);
            });
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<PostDTO> getAllOrderByTotalCommentDesc() {
        try {
            List<PostDTO> list = new ArrayList<>();
            List<PostEntity> postEntities = postRepository.findByAllOrderByTotalCommentDesc();
            postEntities.forEach(postEntity -> {
                PostDTO postDTO = modelMapper.map(postEntity, PostDTO.class);
                list.add(postDTO);
            });
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
