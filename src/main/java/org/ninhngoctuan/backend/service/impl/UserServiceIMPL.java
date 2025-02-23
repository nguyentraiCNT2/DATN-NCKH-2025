package org.ninhngoctuan.backend.service.impl;

import jakarta.mail.internet.MimeMessage;
import org.modelmapper.ModelMapper;
import org.ninhngoctuan.backend.config.PasswordValidator;
import org.ninhngoctuan.backend.config.RandomId;
import org.ninhngoctuan.backend.config.SendCodeByEmail;
import org.ninhngoctuan.backend.config.TemplateConfig;
import org.ninhngoctuan.backend.context.RequestContext;
import org.ninhngoctuan.backend.dto.EmailActiveDTO;
import org.ninhngoctuan.backend.dto.PasswordResetDTO;
import org.ninhngoctuan.backend.dto.ProfileDTO;
import org.ninhngoctuan.backend.dto.UserDTO;
import org.ninhngoctuan.backend.entity.*;
import org.ninhngoctuan.backend.repository.*;
import org.ninhngoctuan.backend.service.FirebaseStorageService;
import org.ninhngoctuan.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceIMPL implements UserService {
    @Value("${images.dir}")
    private String imagesDir;
    private final JavaMailSender mailSender;
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private RoleRepository roleRepository;
    private EmailActiveRepository emailActiveRepository;
    private PasswordResetRepository passwordResetRepository;
    private FirebaseStorageService firebaseStorageService;
    private FriendsRepository friendsRepository;
    private final ProfileRepository profileRepository;

    public UserServiceIMPL(JavaMailSender mailSender, FriendsRepository friendsRepository, FirebaseStorageService firebaseStorageService, PasswordResetRepository passwordResetRepository, EmailActiveRepository emailActiveRepository,  RoleRepository roleRepository, ModelMapper modelMapper, UserRepository userRepository, ProfileRepository profileRepository) {
        this.mailSender = mailSender;
        this.friendsRepository = friendsRepository;
        this.firebaseStorageService = firebaseStorageService;
        this.passwordResetRepository = passwordResetRepository;
        this.emailActiveRepository = emailActiveRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public UserDTO register(UserDTO userDTO) {
        try {
            if (userDTO.getEmail() == null || userDTO.getEmail().equals(""))
                throw new RuntimeException("Email không thể để trống");
            if (userDTO.getPassword() == null || userDTO.getPassword().equals(""))
                throw new RuntimeException("Mật khẩu không thể để trống");
            if (userDTO.getPassword().length()< 6)
                throw new RuntimeException("Mật khẩu có tối thiểu 6 ký tự");
            if (userDTO.getPassword().length()> 255)
                throw new RuntimeException("Mật khẩu có tối đa 255 ký tự");
            if (userDTO.getPassword().contains(" "))
                throw new RuntimeException("Mật khẩu không thể chứa khoản trắng");
            if (!PasswordValidator.isValidPassword(userDTO.getPassword()))
                throw  new RuntimeException("Mật khẩu phải chứa chữ hoa, chữ thường và số");
            if (userDTO.getFullName() == null || userDTO.getFullName().equals(""))
                throw new RuntimeException("Họ tên không thể để trống");
            boolean checkEmail = userRepository.existsByEmail(userDTO.getEmail());
            if (checkEmail)
                throw new RuntimeException("Email này đã được sử dụng");
            RoleEntity role = roleRepository.findById(2L).orElseThrow(() -> new RuntimeException("Không có quyền hạn nào có id là: "+2));
            Date currentDate = new Date(System.currentTimeMillis());

            UserEntity user = modelMapper.map(userDTO, UserEntity.class);
            String hashPassword =  BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setCreatedAt(currentDate);
            user.setPassword(hashPassword);
            user.setActive(true);
            user.setEmailActive(false);
            user.setUpdatedAt(currentDate);
            user.setRoleId(role);
            UserEntity save_entity = userRepository.save(user);
            ProfileEntity profile = new ProfileEntity();
            profile.setUser(user);
            profile.setEmail(userDTO.getEmail());
            profile.setPhone(userDTO.getPhone());
            profileRepository.save(profile);
            UserDTO dto = modelMapper.map(save_entity,UserDTO.class);
            sendEmail(dto);
            return  dto;

        }catch (Exception e){
            throw new RuntimeException("Có lỗi sảy ra: "+e.getMessage());
        }
    }

    @Override
    public boolean sendEmail(UserDTO userDTO) {
        try {
            String code  = RandomId.generateMKC2(6);
            String to = userDTO.getEmail();
            String subject = "Xác thực email đăng ký ";
            String body = TemplateConfig.TEMPALTE_EMAIL_VARIFIED.replace("{{otp_code}}", code);

                UserEntity userEntity  = modelMapper.map(userDTO, UserEntity.class);
                EmailActiveEntity entity = new EmailActiveEntity();
                entity.setEmail(userDTO.getEmail());
                entity.setOtp(code);
                entity.setUserId(userEntity);
                emailActiveRepository.save(entity);

                    new Thread (() -> sendEmailAsync (to, subject, body, code)).start ();
                return true;
        }catch (Exception e){
            throw new RuntimeException("Có lỗi sảy ra: "+e.getMessage());
        }
    }

    @Override
    public boolean reSendEmail(String email) {
        try {
            UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));
            String code  = RandomId.generateMKC2(6);
            String to = user.getEmail();
            String subject = "Xác thực email đăng ký ";
            String body = TemplateConfig.TEMPALTE_EMAIL_VARIFIED.replace("{{otp_code}}", code);

            EmailActiveEntity entity = new EmailActiveEntity();
            entity.setEmail(user.getEmail());
            entity.setOtp(code);
            entity.setUserId(user);
            emailActiveRepository.save(entity);

            new Thread (() -> sendEmailAsync (to, subject, body, code)).start ();
            return true;
        }catch (Exception e){
            throw new RuntimeException("Có lỗi sảy ra: "+e.getMessage());
        }
    }

    @Override
    public boolean activeEmail(EmailActiveDTO emailActiveDTO) {
        List<EmailActiveEntity> emailActiveEntities  = emailActiveRepository.findByEmail(emailActiveDTO.getEmail());
        if (emailActiveEntities.size() == 0)
            throw new RuntimeException("Không có tài khoản nào có email này");
        for (EmailActiveEntity active: emailActiveEntities){
            if (active.getOtp().equals(emailActiveDTO.getOtp())){
                UserEntity userEntity = userRepository.findByEmail(emailActiveDTO.getEmail()).orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
                if (userEntity == null)
                    throw new RuntimeException("Không có tài khoản nào có email là: "+emailActiveDTO.getEmail());
                userEntity.setEmailActive(true);
                userRepository.save(userEntity);
                return true;
            }
        }
        return false;
    }

    @Override
    public UserDTO info(String username) {
        UserEntity userEntity = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
     UserDTO userDTO =  modelMapper.map(userEntity, UserDTO.class);
        userDTO.setPassword(null);
        return userDTO;
    }

    @Override
    public UserDTO foGetPasword(String username) {
        UserEntity  userEntity = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
        if (userEntity == null) {
            throw new UsernameNotFoundException("Tài khoản không tồn tại");
        }
        UserDTO dto = modelMapper.map(userEntity,UserDTO.class);
        sendEmailForgetPassword(dto);
        return dto;
    }

    @Override
    public boolean resetPassword(String email,String newPassword, String confirmPassword) {
        try {

            if (newPassword == null || newPassword=="")
                throw new RuntimeException("Mật khẩu không thể để trống");
             if (confirmPassword == null || confirmPassword == "")
                throw new RuntimeException("Bạn chưa xác nhận mật khẩu");
            if (!newPassword.equals(confirmPassword))
                throw new RuntimeException("Mật khẩu không trùng khớp");
            if (newPassword.length() < 6)
                throw new RuntimeException("Mật khẩu có tối thiểu 6 ký tự");
            if (newPassword.length() > 255 )
                throw new RuntimeException("Mật khẩu có tối đa 255 ký tự");
            if (!PasswordValidator.isValidPassword(newPassword))
                throw  new RuntimeException("Mật khẩu phải chứa chữ hoa, chữ thường và số");
            if (newPassword.contains(" "))
                throw new RuntimeException("Mật khẩu không thể chứa khoảng trắng");
            UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
            user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            userRepository.save(user);
          return true;

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean sendEmailForgetPassword(UserDTO userDTO) {
        try {
            String code  = RandomId.generateMKC2(6);
            String to = userDTO.getEmail();
            String subject = "Xác thực email quên mật khẩu ";
            String body = TemplateConfig.TEMPLATE_PASSWORD_FOR_GET.replace("{{otp_code}}", code);

            UserEntity userEntity  = modelMapper.map(userDTO, UserEntity.class);
            PasswordResetEntity entity = new PasswordResetEntity();
            entity.setEmail(userDTO.getEmail());
            entity.setOtp(code);
            entity.setPassword(userEntity.getPassword());

            passwordResetRepository.save(entity);

            new Thread (() -> sendEmailAsync (to, subject, body, code)).start ();
            return true;
        }catch (Exception e){
            throw new RuntimeException("Có lỗi sảy ra: "+e.getMessage());
        }
    }

    @Override
    public boolean reSendEmailForgetPassword(String email) {
        try {
            UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));
            String code  = RandomId.generateMKC2(6);
            String to = user.getEmail();
            String subject = "Xác thực email quên mật khẩu ";
            String body = TemplateConfig.TEMPLATE_PASSWORD_FOR_GET.replace("{{otp_code}}", code);
            PasswordResetEntity entity = new PasswordResetEntity();
            entity.setEmail(user.getEmail());
            entity.setOtp(code);
            entity.setPassword(user.getPassword());
            passwordResetRepository.save(entity);

            new Thread (() -> sendEmailAsync (to, subject, body, code)).start ();
            return true;
        }catch (Exception e){
            throw new RuntimeException("Có lỗi sảy ra: "+e.getMessage());
        }
    }

    @Override
    public boolean activeEmailForgetPassword(PasswordResetDTO passwordResetDTO) {
        try {
            List<PasswordResetEntity> passwordResetEntities = passwordResetRepository.findByEmail(passwordResetDTO.getEmail());
            passwordResetEntities.stream()
                    .filter(passwordResetEntity -> passwordResetEntity.getOtp().equals(passwordResetDTO.getPassword()))
                    .forEach(passwordResetEntity -> {
                        UserEntity user = userRepository.findByEmail(passwordResetEntity.getEmail())
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng có email: "+ passwordResetEntity.getEmail()));
                    });
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public UserDTO identifyPassword(PasswordResetDTO passwordResetDTO) {
        try {
            UserDTO dto = new UserDTO();
            UserEntity userEntity = userRepository.findByEmail(passwordResetDTO.getEmail()).orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

           List<PasswordResetEntity> passwordReset = passwordResetRepository.findByEmail(passwordResetDTO.getEmail());
           for (PasswordResetEntity passwordResetEntity : passwordReset) {
               if (passwordResetEntity.getOtp().equals(passwordResetDTO.getOtp())) {
                   userEntity.setPassword(passwordResetDTO.getPassword());
                  UserEntity save_user= userRepository.save(userEntity);
                   dto = modelMapper.map(save_user, UserDTO.class);
               }
           }
           dto.setPassword(null);
           return dto;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ProfileDTO me() {
        try {
            String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
            ProfileEntity profile = profileRepository.findByUser(user.getUserId()).orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin cá nhân"));
            ProfileDTO dto =  modelMapper.map(profile, ProfileDTO.class);

            return dto;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ProfileDTO profileUser(Long id) {

        try {
            UserEntity user = userRepository.findByUserId(id).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
            ProfileEntity profile = profileRepository.findByUser(user.getUserId()).orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin cá nhân"));
            ProfileDTO dto =  modelMapper.map(profile, ProfileDTO.class);
            return dto;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void changePassword(String oldPassword, String newPassword, String confirmPassword) {
        String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
        boolean isMatch = BCrypt.checkpw(oldPassword, user.getPassword());
       if (oldPassword == null || oldPassword == "")
           throw new RuntimeException("Bạn chưa nhập mật khẩu hiển tại");
       if (newPassword == null || newPassword == "")
           throw  new RuntimeException("Bạn chưa nhập mật khẩu mới");
       if(confirmPassword == null || confirmPassword == "")
           throw new RuntimeException("Bạn chưa nhập xác nhận mật khẩu");
        if (!isMatch)
            throw  new RuntimeException("Mật khẩu cũ không chính xác ");
        if (oldPassword.equals(newPassword))
            throw new RuntimeException("Mật khẩu cữ không dược trùng với mật khẩu mới");
        if (newPassword.length() < 6)
            throw new RuntimeException("Mật khẩu có tối thiểu 6 ký tự");
        if (newPassword.length() > 255 )
            throw new RuntimeException("Mật khẩu có tối đa 255 ký tự");
        if (!PasswordValidator.isValidPassword(newPassword))
            throw  new RuntimeException("Mật khẩu phải chứa chữ hoa, chữ thường và số");
        if (newPassword.contains(" "))
            throw new RuntimeException("Mật khẩu không thể chứa khoảng trắng");
        if (!newPassword.equals(confirmPassword))
            throw  new RuntimeException("mật khẩu không trùng khớp");
        String hashPassword =  BCrypt.hashpw(newPassword, BCrypt.gensalt());
        user.setPassword(hashPassword);
        userRepository.save(user);
    }

    @Override
    public UserDTO unActiveUser() {
        String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
        user.setActive(false);
       UserEntity saveuser= userRepository.save(user);
        return modelMapper.map(saveuser, UserDTO.class);
    }

    @Override
    public UserDTO ActiveUser(Long id) {
        UserEntity user = userRepository.findByUserId(id).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        user.setActive(true);
        UserEntity saveuser= userRepository.save(user);
        return modelMapper.map(saveuser, UserDTO.class);
    }

    @Override
    public UserDTO blockUser(Long id) {
        UserEntity user = userRepository.findByUserId(id).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        user.setActive(false);
        UserEntity saveuser= userRepository.save(user);
        return modelMapper.map(saveuser, UserDTO.class);
    }

    @Override
    public UserDTO updateProfile(ProfileDTO userDTO) {
        try {
            if (userDTO.getPhone() == null || userDTO.getPhone() == "")
                throw new RuntimeException("Bạn chưa nhập số điện thoại");
            String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
            user.setFullName(userDTO.getFirstName() +" "+userDTO.getLastName());
            user.setPhone(userDTO.getPhone());
            user.setEmail(user.getEmail());
            UserEntity saveuser= userRepository.save(user);
            ProfileEntity profile = profileRepository.findByUser(user.getUserId()).orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin cá nhân"));
            profile.setEmail(user.getEmail());
            profile.setUser(saveuser);
            profile.setDescription(userDTO.getDescription());
            profile.setCity(userDTO.getCity());
            profile.setNickName(userDTO.getNickName());
            profile.setFirstName(userDTO.getFirstName());
            profile.setLastName(userDTO.getLastName());
            profile.setRelationship(userDTO.getRelationship());
            profile.setUniversity(userDTO.getUniversity());
            profile.setBirthday(userDTO.getBirthday());
            profile.setPhone(userDTO.getPhone());
            profile.setAddress(userDTO.getAddress());
            profile.setSchool(userDTO.getSchool());
            profile.setState(userDTO.getState());
            profile.setGender(userDTO.getGender());
            profile.setZip(userDTO.getZip());
            profile.setCountry(userDTO.getCountry());
            profileRepository.save(profile);
            return  modelMapper.map(saveuser, UserDTO.class);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void updateProfilePicture(MultipartFile file) {
        try {
            String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
            Path uploadPathImages = Paths.get(imagesDir);

            if (!Files.exists(uploadPathImages)) {
                Files.createDirectories(uploadPathImages);
            }

            if (file != null && !file.isEmpty()) {
                        String imageFileName = firebaseStorageService.uploadFile(file);
                        user.setProfilePicture(imageFileName);
                        userRepository.save(user);
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void updateCoverPicture(MultipartFile file) {
        try {
            String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
            Path uploadPathImages = Paths.get(imagesDir);

            if (!Files.exists(uploadPathImages)) {
                Files.createDirectories(uploadPathImages);
            }

            if (file != null && !file.isEmpty()) {
                String imageFileName = firebaseStorageService.uploadFile(file);
                    user.setCoverPicture(imageFileName);
                    userRepository.save(user);
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<UserDTO> getAllUsers(Pageable pageable) {
        List<UserDTO>list = new ArrayList<>();
        String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
        List<UserEntity> userEntities = userRepository.findAll(pageable).getContent();
        for (UserEntity userEntity : userEntities) {
            if (user.getUserId() != userEntity.getUserId()){
                list.add(modelMapper.map(userEntity, UserDTO.class));
            }
        }
        return list;
    }

    @Override
    public List<UserDTO> getUserByRole(Long roleId, Pageable pageable) {
        List<UserDTO>list = new ArrayList<>();
        RoleEntity role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Không tìm thấy quyền hạn"));
        String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));

        List<UserEntity> userEntities = userRepository.findByRoleId(role,pageable);
        for (UserEntity userEntity : userEntities) {
            if (user.getUserId() != userEntity.getUserId()){
                list.add(modelMapper.map(userEntity, UserDTO.class));
            }
        }
        return list;
    }

    @Override
    public UserDTO getUserById(Long id) {
        try {
            UserEntity userEntity = userRepository.findByUserId(id).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
           return modelMapper.map(userEntity, UserDTO.class);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public UserDTO updateRole(Long id, Long roleId) {
        try {
            RoleEntity role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Không tìm thấy quyền hạn"));
            UserEntity userEntity = userRepository.findByUserId(id).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
            userEntity.setRoleId(role);
         UserEntity save  =   userRepository.save(userEntity);
         return  modelMapper.map(save, UserDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public int totalItem() {
        return (int) userRepository.count();
    }

    @Override
    public List<UserDTO> filterUsers(String fullName, String email, String phone, boolean isActive, boolean isEmailActive, Pageable pageable) {
      try {
          List<UserDTO>list = new ArrayList<>();
          String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
          UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));

          List<UserEntity> userEntities =userRepository.filterUsers(fullName,email,phone,isActive,isEmailActive, pageable);
          for (UserEntity userEntity : userEntities) {
              if (user.getUserId() != userEntity.getUserId()){
                  list.add( modelMapper.map(userEntity, UserDTO.class));

              }
          }
          return list;
      }catch (Exception e){
          throw new RuntimeException(e.getMessage());
      }
    }

    @Override
    public List<UserDTO> getAll(Pageable pageable) {
        try {
            List<UserDTO> list = new ArrayList<>();

            // Lấy danh sách tất cả người dùng
            List<UserEntity> userEntities = userRepository.findAll(pageable).getContent();

            // Lấy người dùng hiện tại
            String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));


            // Lấy danh sách bạn bè
            List<FriendEntity> friendEntities = friendsRepository.findByUser(user);

            // Tạo Set chứa userId của bạn bè để kiểm tra nhanh
            Set<Long> friendIds = friendEntities.stream()
                    .map(friendEntity -> friendEntity.getFriend().getUserId())
                    .collect(Collectors.toSet());

            // Lọc những người dùng không phải là bản thân và không phải là bạn bè
            userEntities.stream()
                    .filter(userEntity -> !Objects.equals(user.getUserId(), userEntity.getUserId())) // Không phải bản thân
                    .filter(userEntity -> !friendIds.contains(userEntity.getUserId())) // Không phải bạn bè
                    .forEach(userEntity -> list.add(modelMapper.map(userEntity, UserDTO.class)));

            return list;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @Override
    public List<UserDTO> getAll() {
        try {
            List<UserDTO> list = new ArrayList<>();

            // Lấy danh sách tất cả người dùng
            List<UserEntity> userEntities = userRepository.findAll();
            if (userEntities.size() == 0)
                throw new RuntimeException("Không có người dùng nào");

            // Lấy người dùng hiện tại
            String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));


            // Lấy danh sách bạn bè
            List<FriendEntity> friendEntities = friendsRepository.findByUser(user);

            // Tạo Set chứa userId của bạn bè để kiểm tra nhanh
            Set<Long> friendIds = friendEntities.stream()
                    .map(friendEntity -> friendEntity.getFriend().getUserId())
                    .collect(Collectors.toSet());

            // Lọc những người dùng không phải là bản thân và không phải là bạn bè
            userEntities.stream()
                    .filter(userEntity -> !Objects.equals(user.getUserId(), userEntity.getUserId())) // Không phải bản thân
                    .filter(userEntity -> !friendIds.contains(userEntity.getUserId())) // Không phải bạn bè
                    .forEach(userEntity -> list.add(modelMapper.map(userEntity, UserDTO.class)));

            return list;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<UserDTO> getAllSupperAdmin() {
        try {
            String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
            List<UserDTO> list = new ArrayList<>();
            List<UserEntity> userEntities  = userRepository.findAll();
                    userEntities.stream()
                            .filter(userEntity -> !userEntity.getUserId().equals(user.getUserId()))
                            .forEach(userEntity -> {
                                list.add(modelMapper.map(userEntity, UserDTO.class));
                            });
                    return list;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<UserDTO> getByName(String fullname) {
        try {
            List<UserDTO>list = new ArrayList<>();
            String search = "%"+fullname+"%";
            List<UserEntity> userEntities =userRepository.findByFullNameLike(search);
            String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
            // Lấy danh sách bạn bè
            List<FriendEntity> friendEntities = friendsRepository.findByUser(user);



            // Lọc những người dùng không phải là bản thân và không phải là bạn bè
            userEntities.stream()
                    .filter(userEntity -> !Objects.equals(user.getUserId(), userEntity.getUserId())) // Không phải bản thân
                    .forEach(userEntity -> list.add(modelMapper.map(userEntity, UserDTO.class)));

            return list;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @Async
    public void sendEmailAsync(String to, String subject, String body, String otpCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // `true` để gửi email dạng HTML
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void lockUser(Long id) {
        try {
            UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Khồng tìm thấy người dùng"));
            user.setActive(false);
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void unlockUser(Long id) {
        try {
            UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Khồng tìm thấy người dùng"));
            user.setActive(true);
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
