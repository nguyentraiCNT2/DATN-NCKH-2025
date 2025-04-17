package org.ninhngoctuan.backend.service;

import org.ninhngoctuan.backend.Requests.RegisterRequest;
import org.ninhngoctuan.backend.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserDTO register(RegisterRequest registerRequest);
    boolean sendEmail(UserDTO userDTO);
    boolean reSendEmail(String email);
    boolean activeEmail(EmailActiveDTO emailActiveDTO);
    UserDTO info(String username);
    UserDTO foGetPasword(String username);
    boolean sendEmailForgetPassword(UserDTO userDTO);
    boolean reSendEmailForgetPassword(String email);
    boolean activeEmailForgetPassword(PasswordResetDTO passwordResetDTO);
    boolean resetPassword(String email,String newPassword, String confirmPassword);
    UserDTO identifyPassword(PasswordResetDTO passwordResetDTO);
    ProfileDTO me();
    ProfileDTO profileUser(Long id);
    void changePassword(String oldPassword, String newPassword, String confirmPassword);
    UserDTO unActiveUser();
    UserDTO ActiveUser(Long id);
    UserDTO blockUser(Long id);
    UserDTO updateProfile(ProfileDTO userDTO);
    void updateProfilePicture(MultipartFile file);
    void updateCoverPicture(MultipartFile file);
    List<UserDTO> getAllUsers(Pageable pageable);
    List<UserDTO> getUserByRole(Long roleId, Pageable pageable);
    UserDTO getUserById(Long id);
    UserDTO updateRole(Long id, Long roleId);
    int totalItem();
    List<UserDTO> filterUsers(String fullName, String email, String phone, boolean isActive, boolean isEmailActive, Pageable pageable);
    List<UserDTO> getAll(Pageable pageable);
    List<UserDTO> getAll();
    List<UserDTO> getAllSupperAdmin();
    List<UserDTO> getByName(String fullname);
    void lockUser(Long id);
    void unlockUser(Long id);

}
