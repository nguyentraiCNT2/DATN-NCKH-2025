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
import java.util.Random;
import java.util.Arrays;
import java.util.List;

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

    private final Random random = new Random();

    // Danh sách tên và họ phổ biến ở Việt Nam
    private final List<String> firstNames = Arrays.asList(
            "Anh", "Bình", "Cường", "Dũng", "Hà", "Hải", "Hằng", "Hiền",
            "Hòa", "Hùng", "Hương", "Lan", "Linh", "Mai", "Nam", "Ngọc",
            "Nhung", "Phương", "Quân", "Thảo", "Thắng", "Thúy", "Tùng", "Vân"
    );
    private final List<String> lastNames = Arrays.asList(
            "Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Huỳnh", "Phan", "Vũ",
            "Võ", "Đặng", "Bùi", "Đỗ", "Hồ", "Ngô", "Dương", "Lý"
    );
    private final List<String> cities = Arrays.asList(
            "Hà Nội", "TP. Hồ Chí Minh", "Đà Nẵng", "Hải Phòng", "Cần Thơ",
            "Huế", "Nha Trang", "Vũng Tàu", "Quy Nhơn", "Đà Lạt"
    );
    private final List<String> streets = Arrays.asList(
            "Trần Hưng Đạo", "Nguyễn Huệ", "Lê Lợi", "Phạm Ngũ Lão", "Nguyễn Trãi",
            "Hùng Vương", "Lý Thường Kiệt", "Điện Biên Phủ", "Ngô Quyền", "Bạch Đằng"
    );
    private final List<String> schools = Arrays.asList(
            "THPT Chu Văn An", "THPT Lê Quý Đôn", "THPT Nguyễn Trãi", "THPT Kim Liên",
            "THPT Trần Phú", "THPT Việt Đức", "THPT Thăng Long"
    );
    private final List<String> universities = Arrays.asList(
            "ĐH Quốc gia Hà Nội", "ĐH Bách Khoa", "ĐH Kinh tế Quốc dân", "ĐH Sư phạm Hà Nội",
            "ĐH Y Hà Nội", "ĐH FPT", "ĐH RMIT", "ĐH Tôn Đức Thắng"
    );
    private final List<String> relationships = Arrays.asList(
            "Độc thân", "Đang hẹn hò", "Đã kết hôn", "Phức tạp"
    );

    // Hàm chuyển đổi tiếng Việt có dấu thành không dấu
    private String removeDiacritics(String str) {
        str = str.toLowerCase();
        str = str.replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a");
        str = str.replaceAll("[èéẹẻẽêềếệểễ]", "e");
        str = str.replaceAll("[ìíịỉĩ]", "i");
        str = str.replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o");
        str = str.replaceAll("[ùúụủũưừứựửữ]", "u");
        str = str.replaceAll("[ỳýỵỷỹ]", "y");
        str = str.replaceAll("đ", "d");
        str = str.replaceAll("\\s+", "");
        return str;
    }

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
            String hashPassword = BCrypt.hashpw("admin123", BCrypt.gensalt());
            UserEntity user = new UserEntity();
            String fullName = "Nguyễn Văn Quản Trị";
            String email = removeDiacritics(fullName) + "@gmail.com";
            user.setEmail(email);
            user.setPassword(hashPassword);
            user.setActive(true);
            user.setEmailActive(true);
            user.setPhone("0987654321");
            user.setFullName(fullName);
            user.setCreatedAt(new Date());
            user.setRoleId(superAdminRole);
            user.setUpdatedAt(new Date());
            UserEntity savedUser = userRepository.save(user);

            ProfileEntity profile = new ProfileEntity();
            profile.setUser(savedUser);
            profile.setEmail(savedUser.getEmail());
            profile.setPhone(savedUser.getPhone());
            profile.setFirstName("Quản Trị");
            profile.setLastName("Nguyễn");
            profile.setAddress("123 Trần Hưng Đạo");
            profile.setCity("Hà Nội");
            profile.setState("Hà Nội");
            profile.setZip("100000");
            profile.setCountry("Việt Nam");
            profile.setGender("Nam");
            try {
                profile.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse("1985-05-15"));
            } catch (Exception e) {
                profile.setBirthday(new Date());
            }
            profile.setSchool("THPT Chu Văn An");
            profile.setUniversity("ĐH Bách Khoa Hà Nội");
            profile.setRelationship("Đã kết hôn");
            profile.setNickName("SuperAdmin");
            profile.setDescription("Quản trị viên hệ thống");
            profileRepository.save(profile);
        }

        // Create additional admin and user accounts if user count < 10
        if (userRepository.count() < 10) {
            String defaultUserPassword = BCrypt.hashpw("User@2025", BCrypt.gensalt());
            String defaultAdminPassword = BCrypt.hashpw("Admin@2025", BCrypt.gensalt());
            RoleEntity adminRole = roleRepository.findById(2L).get();
            RoleEntity userRole = roleRepository.findById(3L).get();

            // Create 4 admin accounts
            for (int i = 1; i <= 4; i++) {
                String firstName = firstNames.get(random.nextInt(firstNames.size()));
                String lastName = lastNames.get(random.nextInt(lastNames.size()));
                String fullName = lastName + " " + firstName;
                String email = removeDiacritics(fullName) + i + "@gmail.com";
                UserEntity admin = new UserEntity();
                admin.setEmail(email);
                admin.setPassword(defaultAdminPassword);
                admin.setActive(true);
                admin.setEmailActive(true);
                admin.setPhone("09" + String.format("%08d", random.nextInt(100000000)));
                admin.setFullName(fullName);
                admin.setCreatedAt(new Date());
                admin.setUpdatedAt(new Date());
                admin.setRoleId(adminRole);
                UserEntity savedAdmin = userRepository.save(admin);

                ProfileEntity adminProfile = new ProfileEntity();
                adminProfile.setUser(savedAdmin);
                adminProfile.setEmail(savedAdmin.getEmail());
                adminProfile.setPhone(savedAdmin.getPhone());
                adminProfile.setFirstName(firstName);
                adminProfile.setLastName(lastName);
                adminProfile.setAddress(random.nextInt(1000) + " " + streets.get(random.nextInt(streets.size())));
                adminProfile.setCity(cities.get(random.nextInt(cities.size())));
                adminProfile.setState(adminProfile.getCity());
                adminProfile.setZip("10" + String.format("%03d", random.nextInt(1000)));
                adminProfile.setCountry("Việt Nam");
                adminProfile.setGender(random.nextBoolean() ? "Nam" : "Nữ");
                try {
                    adminProfile.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(
                            (1985 + random.nextInt(15)) + "-" + String.format("%02d", random.nextInt(12) + 1) + "-" + String.format("%02d", random.nextInt(28) + 1)
                    ));
                } catch (Exception e) {
                    adminProfile.setBirthday(new Date());
                }
                adminProfile.setSchool(schools.get(random.nextInt(schools.size())));
                adminProfile.setUniversity(universities.get(random.nextInt(universities.size())));
                adminProfile.setRelationship(relationships.get(random.nextInt(relationships.size())));
                adminProfile.setNickName(firstName + i);
                adminProfile.setDescription("Quản trị viên " + fullName);
                profileRepository.save(adminProfile);
            }

            // Create 15 user accounts
            for (int i = 1; i <= 15; i++) {
                String firstName = firstNames.get(random.nextInt(firstNames.size()));
                String lastName = lastNames.get(random.nextInt(lastNames.size()));
                String fullName = lastName + " " + firstName;
                String email = removeDiacritics(fullName) + i + "@gmail.com";
                UserEntity user = new UserEntity();
                user.setEmail(email);
                user.setPassword(defaultUserPassword);
                user.setActive(true);
                user.setEmailActive(true);
                user.setPhone("09" + String.format("%08d", random.nextInt(100000000)));
                user.setFullName(fullName);
                user.setCreatedAt(new Date());
                user.setUpdatedAt(new Date());
                user.setRoleId(userRole);
                UserEntity savedUser = userRepository.save(user);

                ProfileEntity userProfile = new ProfileEntity();
                userProfile.setUser(savedUser);
                userProfile.setEmail(savedUser.getEmail());
                userProfile.setPhone(savedUser.getPhone());
                userProfile.setFirstName(firstName);
                userProfile.setLastName(lastName);
                userProfile.setAddress(random.nextInt(1000) + " " + streets.get(random.nextInt(streets.size())));
                userProfile.setCity(cities.get(random.nextInt(cities.size())));
                userProfile.setState(userProfile.getCity());
                userProfile.setZip("20" + String.format("%03d", random.nextInt(1000)));
                userProfile.setCountry("Việt Nam");
                userProfile.setGender(random.nextBoolean() ? "Nam" : "Nữ");
                try {
                    userProfile.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(
                            (1990 + random.nextInt(15)) + "-" + String.format("%02d", random.nextInt(12) + 1) + "-" + String.format("%02d", random.nextInt(28) + 1)
                    ));
                } catch (Exception e) {
                    userProfile.setBirthday(new Date());
                }
                userProfile.setSchool(schools.get(random.nextInt(schools.size())));
                userProfile.setUniversity(universities.get(random.nextInt(universities.size())));
                userProfile.setRelationship(relationships.get(random.nextInt(relationships.size())));
                userProfile.setNickName(firstName + i);
                userProfile.setDescription("Hồ sơ của " + fullName);
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