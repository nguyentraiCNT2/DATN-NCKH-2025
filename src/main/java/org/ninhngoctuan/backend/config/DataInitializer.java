package org.ninhngoctuan.backend.config;

import jakarta.annotation.PostConstruct;
import org.ninhngoctuan.backend.entity.ProfileEntity;
import org.ninhngoctuan.backend.entity.RoleEntity;
import org.ninhngoctuan.backend.entity.TagEntity;
import org.ninhngoctuan.backend.entity.UserEntity;
import org.ninhngoctuan.backend.repository.ProfileRepository;
import org.ninhngoctuan.backend.repository.RoleRepository;
import org.ninhngoctuan.backend.repository.TagRepository;
import org.ninhngoctuan.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class DataInitializer {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ProfileRepository profileRepository;

    @PostConstruct
    public void init() {
        // Initialize roles if none exist
        if (roleRepository.count() == 0) {
            RoleEntity superAdminRole = new RoleEntity();
            superAdminRole.setName("SUPER_ADMIN");
            roleRepository.save(superAdminRole);

            RoleEntity adminRole = new RoleEntity();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);

            RoleEntity userRole = new RoleEntity();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }

        // Initialize super admin if no users exist
        if (userRepository.count() == 0) {
            RoleEntity superAdminRole = roleRepository.findById(1L).get();
            String hashPassword = BCrypt.hashpw("admin", BCrypt.gensalt());
            UserEntity user = new UserEntity();
            user.setEmail("admin@gmail.com");
            user.setPassword(hashPassword);
            user.setActive(true);
            user.setEmailActive(true);
            user.setPhone("01012345678");
            user.setFullName("Administrator");
            user.setCreatedAt(new Date());
            user.setRoleId(superAdminRole);
            user.setUpdatedAt(new Date());
            UserEntity savedUser = userRepository.save(user);

            ProfileEntity profile = new ProfileEntity();
            profile.setUser(savedUser);
            profile.setEmail(savedUser.getEmail());
            profile.setPhone(savedUser.getPhone());
            profile.setFirstName("Admin");
            profile.setLastName("Super");
            profile.setAddress("123 Admin Street");
            profile.setCity("Hanoi");
            profile.setState("Hanoi");
            profile.setZip("100000");
            profile.setCountry("Vietnam");
            profile.setGender("Male");
            profile.setBirthday(new Date());
            profile.setSchool("Admin School");
            profile.setUniversity("Admin University");
            profile.setRelationship("Single");
            profile.setNickName("SuperAdmin");
            profile.setDescription("Super Administrator Profile");
            profileRepository.save(profile);
        }

        // Create additional admin and user accounts if user count < 10
        if (userRepository.count() < 10) {
            String defaultUserPassword = BCrypt.hashpw("UserCloneNTUk21", BCrypt.gensalt());
            String defaultAdminPassword = BCrypt.hashpw("AdminCloneNTUk21", BCrypt.gensalt());
            RoleEntity adminRole = roleRepository.findById(2L).get();
            RoleEntity userRole = roleRepository.findById(3L).get();

            // Create 4 admin accounts
            for (int i = 1; i <= 4; i++) {
                UserEntity admin = new UserEntity();
                admin.setEmail("admin" + i + "@gmail.com");
                admin.setPassword(defaultAdminPassword);
                admin.setActive(true);
                admin.setEmailActive(true);
                admin.setPhone("09012345" + String.format("%03d", i));
                admin.setFullName("Admin " + i);
                admin.setCreatedAt(new Date());
                admin.setUpdatedAt(new Date());
                admin.setRoleId(adminRole);
                UserEntity savedAdmin = userRepository.save(admin);

                ProfileEntity adminProfile = new ProfileEntity();
                adminProfile.setUser(savedAdmin);
                adminProfile.setEmail(savedAdmin.getEmail());
                adminProfile.setPhone(savedAdmin.getPhone());
                adminProfile.setFirstName("Admin" + i);
                adminProfile.setLastName("User");
                adminProfile.setAddress("123 Admin St " + i);
                adminProfile.setCity("Hanoi");
                adminProfile.setState("Hanoi");
                adminProfile.setZip("10000" + i);
                adminProfile.setCountry("Vietnam");
                adminProfile.setGender(i % 2 == 0 ? "Male" : "Female");
                try {
                    adminProfile.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-0" + i));
                } catch (Exception e) {
                    adminProfile.setBirthday(new Date());
                }
                adminProfile.setSchool("School " + i);
                adminProfile.setUniversity("University " + i);
                adminProfile.setRelationship("Single");
                adminProfile.setNickName("Admin" + i);
                adminProfile.setDescription("Profile for Admin " + i);
                profileRepository.save(adminProfile);
            }

            // Create 15 user accounts
            for (int i = 1; i <= 15; i++) {
                UserEntity user = new UserEntity();
                user.setEmail("user" + i + "@gmail.com");
                user.setPassword(defaultUserPassword);
                user.setActive(true);
                user.setEmailActive(true);
                user.setPhone("09067890" + String.format("%03d", i));
                user.setFullName("User " + i);
                user.setCreatedAt(new Date());
                user.setUpdatedAt(new Date());
                user.setRoleId(userRole);
                UserEntity savedUser = userRepository.save(user);

                ProfileEntity userProfile = new ProfileEntity();
                userProfile.setUser(savedUser);
                userProfile.setEmail(savedUser.getEmail());
                userProfile.setPhone(savedUser.getPhone());
                userProfile.setFirstName("User" + i);
                userProfile.setLastName("Regular");
                userProfile.setAddress("456 User St " + i);
                userProfile.setCity("Hanoi");
                userProfile.setState("Hanoi");
                userProfile.setZip("20000" + i);
                userProfile.setCountry("Vietnam");
                userProfile.setGender(i % 2 == 0 ? "Male" : "Female");
                try {
                    userProfile.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse("1995-01-0" + (i % 9 + 1)));
                } catch (Exception e) {
                    userProfile.setBirthday(new Date());
                }
                userProfile.setSchool("User School " + i);
                userProfile.setUniversity("User University " + i);
                userProfile.setRelationship("Single");
                userProfile.setNickName("User" + i);
                userProfile.setDescription("Profile for User " + i);
                profileRepository.save(userProfile);
            }
        }

        // Initialize tags
        if (tagRepository.count() == 0) {
            String[] emotions = {
                    "Vui vẻ", "Hạnh phúc", "Phấn khởi", "Hào hứng", "Bình yên",
                    "Thoải mái", "Tự hào", "Yêu đời", "Đam mê", "Hứng thú", "Hy vọng",
                    "Mệt mỏi", "Buồn bã", "Lo lắng", "Cô đơn", "Giận dữ", "Chán nản",
                    "Bất an", "Thất vọng", "Hoảng sợ", "Căm phẫn", "Nhục nhã",
                    "Lạc lõng", "Trống rỗng", "Suy tư", "Lạnh nhạt", "Ngỡ ngàng",
                    "Thương nhớ", "Ghen tuông", "Phấn khích", "Khao khát", "Biết ơn", "An ủi"
            };

            for (String emotion : emotions) {
                TagEntity tag = new TagEntity();
                tag.setName(emotion);
                tagRepository.save(tag);
            }
        }
    }
}