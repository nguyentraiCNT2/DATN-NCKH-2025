package org.ninhngoctuan.backend.service;

import org.ninhngoctuan.backend.dto.GroupDTO;
import org.ninhngoctuan.backend.dto.GroupMemberDTO;
import org.ninhngoctuan.backend.dto.GroupPostDTO;
import org.ninhngoctuan.backend.dto.UserDTO;
import org.ninhngoctuan.backend.entity.GroupEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GroupService {
    List<GroupDTO> getByUserId();
    List<GroupDTO> getAdminAllGroup();
    GroupDTO getById(long id);
    GroupMemberDTO getMemberById(Long id);
    List<GroupPostDTO> getPostsByGroupId(Long groupId);
    List<GroupPostDTO> getAllPostsByGroup();
    List<GroupPostDTO> getPostsByGroupIdNotApproved(Long groupId);
    List<UserDTO> getAllMembers(Long groupId);
    List<UserDTO> getAllMembersNotActive(Long groupId);
    boolean addGroup(GroupDTO group);
    boolean updateGroup(GroupDTO group);
    boolean deleteGroup(Long groupId);
    boolean restoredGroup(Long groupId);
    boolean addMember(Long groupId, Long memberId);
    boolean joinGroup(Long groupId);
    boolean activeMember(Long groupId, Long memberId);
    boolean quitMember(Long groupId, Long memberId);
    boolean outGroup(Long groupId);
    boolean addPost(GroupPostDTO post, List<MultipartFile> images, List<MultipartFile> videos);
    boolean updatePost(GroupPostDTO post);
    boolean deletePost(Long postId);
    boolean activePost(Long postId);
    boolean changeRoleMember(Long groupId, Long memberId, String roleId);
    boolean uploadImage(Long groupId,MultipartFile file);
    boolean uploadCoverImage(Long groupId,MultipartFile file);

}
