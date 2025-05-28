package org.ninhngoctuan.backend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.ninhngoctuan.backend.context.RequestContext;
import org.ninhngoctuan.backend.dto.GroupDTO;
import org.ninhngoctuan.backend.dto.GroupMemberDTO;
import org.ninhngoctuan.backend.dto.GroupPostDTO;
import org.ninhngoctuan.backend.dto.UserDTO;
import org.ninhngoctuan.backend.entity.GroupEntity;
import org.ninhngoctuan.backend.entity.GroupMemberEntity;
import org.ninhngoctuan.backend.entity.GroupPostEntity;
import org.ninhngoctuan.backend.entity.UserEntity;
import org.ninhngoctuan.backend.repository.*;
import org.ninhngoctuan.backend.service.FirebaseStorageService;
import org.ninhngoctuan.backend.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    private GroupRepository groupRepository;
    private UserRepository userRepository;
    private GroupMemberRepository groupMemberRepository;
    private ModelMapper modelMapper;
    private final GroupPostRepository groupPostRepository;
    private final GroupPostImageRepository groupPostImageRepository;
    private final GroupPostVideoRepository groupPostVideoRepository;
    private final FirebaseStorageService firebaseStorageService;

    public GroupServiceImpl(GroupRepository groupRepository, UserRepository userRepository, GroupMemberRepository groupMemberRepository, ModelMapper modelMapper, GroupPostRepository groupPostRepository, GroupPostImageRepository groupPostImageRepository, GroupPostVideoRepository groupPostVideoRepository, FirebaseStorageService firebaseStorageService) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.modelMapper = modelMapper;
        this.groupPostRepository = groupPostRepository;
        this.groupPostImageRepository = groupPostImageRepository;
        this.groupPostVideoRepository = groupPostVideoRepository;
        this.firebaseStorageService = firebaseStorageService;
    }

    @Override
    public List<GroupDTO> getByUserId() {
        try {
            String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
            List<GroupMemberEntity> groupMemberEntities = groupMemberRepository.findByUser(user);
            List<GroupDTO> dtos = new ArrayList<>();
            groupMemberEntities.stream()
                    .filter(groupMemberEntity -> groupMemberEntity.getGroup().isDeleted() != true)
                    .forEach(groupMemberEntity -> {
                        dtos.add(modelMapper.map(groupMemberEntity.getGroup(), GroupDTO.class));
                    });

            return dtos;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<GroupDTO> getAdminAllGroup() {
        try {
          List<GroupDTO> dtos = new ArrayList<>();
          List<GroupEntity> groupEntities = groupRepository.findAll();
          groupEntities.stream()
                  .forEach(groupEntity -> dtos.add(modelMapper.map(groupEntity, GroupDTO.class)));
          return dtos;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public GroupDTO getById(long id) {
        try {
            GroupEntity groupEntity = groupRepository.findByGroupId(id).orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm"));
            return modelMapper.map(groupEntity, GroupDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public GroupMemberDTO getMemberById(Long id) {
        try {
            String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
            GroupMemberEntity groupMember = groupMemberRepository.findByGroupIdAndUserId(id, user.getUserId());
            return modelMapper.map(groupMember, GroupMemberDTO.class);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<GroupPostDTO> getPostsByGroupId(Long groupId) {
        try {
            List<GroupPostDTO> list = new ArrayList<>();
            List<GroupPostEntity> groupPostEntities = groupPostRepository.findByGroupId(groupId);
            groupPostEntities.stream()
                    .filter(groupPostEntity -> groupPostEntity.getActive())
                    .forEach(groupPostEntity -> {
                        list.add(modelMapper.map(groupPostEntity, GroupPostDTO.class));
                    });
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<GroupPostDTO> getAllPostsByGroup() {
        try {
            List<GroupEntity> groupEntities = new ArrayList<>();
            String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
            List<GroupMemberEntity> groupMemberEntities = groupMemberRepository.findByUser(user);
            groupMemberEntities.stream()
                    .forEach(groupMemberEntity -> {
                        groupEntities.add(groupMemberEntity.getGroup());
                    });
            List<GroupPostDTO> list = new ArrayList<>();
            groupEntities.stream()
                    .forEach(groupEntity -> {
                        List<GroupPostEntity> groupPostEntities = groupPostRepository.findByGroupId(groupEntity.getGroupId());
                        groupPostEntities.stream()
                                .filter(groupPostEntity -> groupPostEntity.getActive())
                                .forEach(groupPostEntity -> {
                                    list.add(modelMapper.map(groupPostEntity, GroupPostDTO.class));
                                });
                    });
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<GroupPostDTO> getPostsByGroupIdNotApproved(Long groupId) {
        try {
            List<GroupPostDTO> list = new ArrayList<>();
            List<GroupPostEntity> groupPostEntities = groupPostRepository.findByGroupId(groupId);
            groupPostEntities.stream()
                    .filter(groupPostEntity -> !groupPostEntity.getActive())
                    .forEach(groupPostEntity -> {
                        list.add(modelMapper.map(groupPostEntity, GroupPostDTO.class));
                    });
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<UserDTO> getAllMembers(Long groupId) {
        try {

            GroupEntity groupEntity = groupRepository.findByGroupId(groupId).orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm"));
            List<GroupMemberEntity> groupMemberEntities = groupMemberRepository.findByGroup(groupEntity);
            List<UserDTO> dtos = new ArrayList<>();
            for (GroupMemberEntity groupMember : groupMemberEntities) {
                if (groupMember.isActive() == true) {
                    dtos.add(modelMapper.map(groupMember.getUser(), UserDTO.class));
                }
            }
            return dtos;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<UserDTO> getAllMembersNotActive(Long groupId) {
        try {
            GroupEntity groupEntity = groupRepository.findByGroupId(groupId).orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm"));
            List<GroupMemberEntity> groupMemberEntities = groupMemberRepository.findByGroup(groupEntity);
            List<UserDTO> dtos = new ArrayList<>();
            for (GroupMemberEntity groupMember : groupMemberEntities) {
                if (groupMember.isActive() == false) {
                    dtos.add(modelMapper.map(groupMember.getUser(), UserDTO.class));
                }
            }
            return dtos;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean addGroup(GroupDTO group) {
        try {
            // Lấy email người dùng hiện tại từ Authentication
            String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            // Tìm người dùng từ cơ sở dữ liệu theo email
            UserEntity user = userRepository.findByEmail(authEmail)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));

            // Chuyển GroupDTO thành GroupEntity và thiết lập các giá trị
            GroupEntity groupEntity = modelMapper.map(group, GroupEntity.class);
            groupEntity.setCreatedAt(new Date());
            groupEntity.setUpdatedAt(new Date());
            groupEntity.setDeleted(false);

            // Lưu nhóm vào cơ sở dữ liệu
            GroupEntity saveGroup = groupRepository.save(groupEntity);

            // Thêm người dùng vào nhóm với vai trò quản trị viên
            GroupMemberEntity groupMemberEntity = new GroupMemberEntity();
            groupMemberEntity.setGroup(saveGroup);
            groupMemberEntity.setUser(user);
            groupMemberEntity.setGroupRole("GROUP_ADMIN");  // Thiết lập vai trò cho người dùng là quản trị viên
            groupMemberEntity.setActive(true);
            groupMemberEntity.setJoinedAt(new Date());

            // Lưu thông tin thành viên nhóm vào cơ sở dữ liệu
            groupMemberRepository.save(groupMemberEntity);

            return true;  // Trả về true nếu việc tạo nhóm và thêm thành viên thành công
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());  // Ném lỗi nếu có sự cố xảy ra
        }
    }


    @Override
    public boolean updateGroup(GroupDTO group) {
        try {
            String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
            GroupEntity groupEntity = groupRepository.findByGroupId(group.getGroupId()).get();
            List<GroupMemberEntity> groupMemberEntities = groupMemberRepository.findByGroup(groupEntity);
            for (GroupMemberEntity groupMemberEntity : groupMemberEntities) {
                if (groupMemberEntity.getGroupRole().equals("GROUP_ADMIN") && groupMemberEntity.getUser().getUserId() == user.getUserId()) {
                    groupEntity.setUpdatedAt(new Date());
                    groupEntity.setName(group.getName());
                    groupEntity.setDescription(group.getDescription());
                    groupRepository.save(groupEntity);
                    return true;
                } else {
                    throw new RuntimeException("Bạn không có quyền hạn để chỉnh sửa nhóm");
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteGroup(Long groupId) {
        try {
            GroupEntity groupEntity = groupRepository.findByGroupId(groupId).orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm"));
            groupEntity.setDeleted(true);
            groupRepository.save(groupEntity);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean restoredGroup(Long groupId) {
        try {
            GroupEntity groupEntity = groupRepository.findByGroupId(groupId).orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm"));
            groupEntity.setDeleted(false);
            groupRepository.save(groupEntity);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public boolean addMember(Long groupId, Long memberId) {
        try {
            String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity userAuth = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
            GroupEntity groupEntity = groupRepository.findByGroupId(groupId).get();
            UserEntity user = userRepository.findByUserId(memberId).get();
            List<GroupMemberEntity> groupMemberEntities = groupMemberRepository.findByGroup(groupEntity);
            for (GroupMemberEntity groupMemberEntity : groupMemberEntities) {
                if (groupMemberEntity.getGroupRole().equals("GROUP_ADMIN") && groupMemberEntity.getUser().getUserId() == userAuth.getUserId()) {
                    if (groupMemberEntity.getUser().getUserId() == user.getUserId()) {
                        throw new RuntimeException("Người này đã có trong nhóm");
                    }
                    GroupMemberEntity groupMember = new GroupMemberEntity();
                    groupMember.setJoinedAt(new Date());
                    groupMember.setGroupRole("GROUP_MEMBER");
                    groupMember.setUser(user);
                    groupMember.setGroup(groupEntity);
                    groupMemberEntity.setActive(true);
                    groupMemberRepository.save(groupMember);
                    return true;
                } else {
                    if (groupMemberEntity.getUser().getUserId() == user.getUserId()) {
                        throw new RuntimeException("Người này đã có trong nhóm");
                    }
                    GroupMemberEntity groupMember = new GroupMemberEntity();
                    groupMember.setJoinedAt(new Date());
                    groupMember.setGroupRole("GROUP_MEMBER");
                    groupMember.setUser(user);
                    groupMember.setGroup(groupEntity);
                    groupMemberEntity.setActive(false);
                    groupMemberRepository.save(groupMember);
                    return true;
                }

            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean joinGroup(Long groupId) {

        try {
            GroupEntity groupEntity = groupRepository.findByGroupId(groupId).orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm"));
            String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
            List<GroupMemberEntity> groupMemberEntities = groupMemberRepository.findByGroup(groupEntity);
            for (GroupMemberEntity groupMemberEntity : groupMemberEntities) {
                if (groupMemberEntity.getUser().getUserId().equals(user.getUserId())) {
                    throw new RuntimeException("Bạn đã có trong nhóm");
                }else {
                    GroupMemberEntity groupMember = new GroupMemberEntity();
                    groupMember.setJoinedAt(new Date());
                    groupMember.setGroupRole("GROUP_MEMBER");
                    groupMember.setUser(user);
                    groupMember.setGroup(groupEntity);
                    groupMember.setActive(false);
                    groupMemberRepository.save(groupMember);
                    return true;
                }


            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean activeMember(Long groupId, Long memberId) {
        // Lấy email của người dùng hiện tại từ SecurityContext
        String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // Tìm người dùng hiện tại
        UserEntity user = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));

        // Tìm nhóm theo groupId
        GroupEntity groupEntity = groupRepository.findByGroupId(groupId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm"));

        // Kiểm tra quyền ADMIN của người dùng
        validateAdminRole(groupEntity, user);

        // Tìm thành viên trong nhóm và kích hoạt
        GroupMemberEntity groupMemberEntity = groupMemberRepository.findByGroupIdAndUserId(groupEntity.getGroupId(), memberId);

        groupMemberEntity.setActive(true);
        groupMemberRepository.save(groupMemberEntity);

        return true;
    }

    private void validateAdminRole(GroupEntity groupEntity, UserEntity user) {
        List<GroupMemberEntity> groupMemberEntities = groupMemberRepository.findByGroup(groupEntity);

        // Kiểm tra xem người dùng có vai trò ADMIN trong nhóm không
        boolean isAdmin = groupMemberEntities.stream()
                .anyMatch(member -> member.getUser().getUserId().equals(user.getUserId())
                        && "GROUP_ADMIN".equals(member.getGroupRole()));

        if (!isAdmin) {
            throw new RuntimeException("Bạn không có quyền hạn để thực hiện chức năng");
        }
    }
    @Override
    public boolean quitMember(Long groupId, Long memberId) {
        // Lấy email của người dùng hiện tại từ SecurityContext
        String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // Tìm người dùng hiện tại
        UserEntity user = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));

        // Tìm nhóm theo groupId
        GroupEntity groupEntity = groupRepository.findByGroupId(groupId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm"));

        // Kiểm tra xem người dùng có phải là thành viên của nhóm không
        GroupMemberEntity groupMemberEntity = groupMemberRepository.findByGroupIdAndUserId(groupEntity.getGroupId(),memberId);
        GroupMemberEntity groupAdminEntity = groupMemberRepository.findByGroupIdAndUserId(groupEntity.getGroupId(),user.getUserId());

        // Kiểm tra quyền ADMIN hoặc xác nhận người dùng tự xóa chính mình
        validateQuitPermission(groupAdminEntity, user.getUserId(), memberId);

        // Xóa thành viên khỏi nhóm
        groupMemberRepository.delete(groupMemberEntity);

        return true;
    }

    private void validateQuitPermission(GroupMemberEntity groupMemberEntity, Long currentUserId, Long targetMemberId) {
        // Kiểm tra xem người dùng có quyền ADMIN hoặc đang tự xóa chính mình không
        if (!currentUserId.equals(targetMemberId) && !"GROUP_ADMIN".equals(groupMemberEntity.getGroupRole())) {
            throw new RuntimeException("Bạn không có quyền hạn để thực hiện chức năng");
        }
    }

    @Override
    public boolean outGroup(Long groupId) {
        try {
            String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
            GroupEntity groupEntity = groupRepository.findByGroupId(groupId).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
            GroupMemberEntity groupMemberEntities = groupMemberRepository.findByGroupIdAndUserId(groupEntity.getGroupId(), user.getUserId());
            if (groupMemberEntities.getUser().getUserId() != user.getUserId())
                throw new RuntimeException("Id đăng nhập không chính xác");
            if (groupMemberEntities.getGroupRole().equals("GROUP_ADMIN")) {
                throw new RuntimeException("Bạn là chủ nhóm nên không thể rời nhóm");
            } else {
                groupMemberRepository.delete(groupMemberEntities);
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean addPost(GroupPostDTO post, List<MultipartFile> images, List<MultipartFile> videos) {
        try {
            String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
            GroupEntity group = groupRepository.findByGroupId(post.getGroup().getGroupId()).orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm"));
            GroupPostEntity groupPostEntity = new GroupPostEntity();
            groupPostEntity.setContent(post.getContent());
            groupPostEntity.setActive(false);
            groupPostEntity.setGroup(group);
            groupPostEntity.setUser(user);
            groupPostEntity.setCreatedAt(Timestamp.from(Instant.now()));
            try {
                // Tạo đối tượng JSON cho hình ảnh
                List<String> imageList = new ArrayList<>();
                List<String> videoList = new ArrayList<>();
                // Lưu danh sách hình ảnh
                if (images != null) {
                    for (MultipartFile imageFile : images) {
                        String imageUrl = firebaseStorageService.uploadFile(imageFile);
                        imageList.add(imageUrl);
                    }
                }
                // Lưu danh sách video
                if (videos != null) {
                    for (MultipartFile videoFile : videos) {
                        String videoUrl = firebaseStorageService.uploadFile(videoFile);
                        videoList.add(videoUrl);
                    }
                }
                ObjectMapper objectMapper = new ObjectMapper();
                String ListImage = objectMapper.writeValueAsString(imageList);
                String ListVideo = objectMapper.writeValueAsString(videoList);

                groupPostEntity.setImageUrl(ListImage);
                groupPostEntity.setVideoUrl(ListVideo);

            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
            groupPostRepository.save(groupPostEntity);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean updatePost(GroupPostDTO post) {
        return false;
    }

    @Override
    public boolean deletePost(Long postId) {
        try {
            GroupPostEntity groupPostEntity = groupPostRepository.findById(postId).orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết"));
            groupPostRepository.delete(groupPostEntity);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean activePost(Long postId) {
        try {
            GroupPostEntity groupPostEntity = groupPostRepository.findById(postId).orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết"));
            groupPostEntity.setActive(true);
            groupPostRepository.save(groupPostEntity);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean changeRoleMember(Long groupId, Long memberId, String roleId) {
        try {
            GroupMemberEntity groupMember = groupMemberRepository.findByGroupIdAndUserId(groupId, memberId);
            groupMember.setGroupRole(roleId);
            groupMemberRepository.save(groupMember);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean uploadImage(Long groupId, MultipartFile file) {
        try {
            String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
            GroupEntity groupEntity = groupRepository.findByGroupId(groupId).orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm "));
            GroupMemberEntity groupMemberEntity = groupMemberRepository.findByGroupIdAndUserId(groupEntity.getGroupId(), user.getUserId());
            if (!groupMemberEntity.getGroupRole().equals("GROUP_ADMIN"))
                throw new RuntimeException("Bạn không đủ quyền hạn để thực hiện chức năng");
            String imageUrl = firebaseStorageService.uploadFile(file);
            groupEntity.setGroupImage(imageUrl);
            groupRepository.save(groupEntity);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean uploadCoverImage(Long groupId, MultipartFile file) {
        try {
            String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
            GroupEntity groupEntity = groupRepository.findByGroupId(groupId).orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm "));
            GroupMemberEntity groupMemberEntity = groupMemberRepository.findByGroupIdAndUserId(groupEntity.getGroupId(), user.getUserId());
            if (!groupMemberEntity.getGroupRole().equals("GROUP_ADMIN"))
                throw new RuntimeException("Bạn không đủ quyền hạn để thực hiện chức năng");
            String imageUrl = firebaseStorageService.uploadFile(file);
            groupEntity.setGroupCoverImage(imageUrl);
            groupRepository.save(groupEntity);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
