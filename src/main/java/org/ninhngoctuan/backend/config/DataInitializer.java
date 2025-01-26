package org.ninhngoctuan.backend.config;

import jakarta.annotation.PostConstruct;
import org.ninhngoctuan.backend.entity.RoleEntity;
import org.ninhngoctuan.backend.entity.TagEntity;
import org.ninhngoctuan.backend.entity.UserEntity;
import org.ninhngoctuan.backend.repository.RoleRepository;
import org.ninhngoctuan.backend.repository.TagRepository;
import org.ninhngoctuan.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Date;

@Configuration
public class DataInitializer {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TagRepository tagRepository;
    @PostConstruct
    public void init() {
        if (roleRepository.count() == 0) {
            RoleEntity adminRole = new RoleEntity();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);

            RoleEntity userRole = new RoleEntity();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }
        if (userRepository.count() == 0) {
            RoleEntity userRole = roleRepository.findById(1L).get();
            String hashPassword =  BCrypt.hashpw("admin", BCrypt.gensalt());
            UserEntity user = new UserEntity();
            user.setEmail("admin@gmail.com");
            user.setPassword(hashPassword);
            user.setActive(true);
            user.setEmailActive(true);
            user.setPhone("01012345678");
            user.setFullName("Adminstrator");
            user.setCreatedAt(new Date());
            user.setRoleId(userRole);
            user.setUpdatedAt(new Date());
            userRepository.save(user);

        }
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
