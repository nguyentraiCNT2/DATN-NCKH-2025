// CustomUserDetailsService.java
package org.ninhngoctuan.backend.securityConfig;

import org.ninhngoctuan.backend.context.RequestContext;
import org.ninhngoctuan.backend.entity.RoleEntity;
import org.ninhngoctuan.backend.entity.UserEntity;
import org.ninhngoctuan.backend.repository.RoleRepository;
import org.ninhngoctuan.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    @Lazy
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity  userEntity = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
        if (userEntity.isActive() == false) {
            throw new UsernameNotFoundException("Tài khoản của bạn đã bị khóa");
        }
        if (userEntity.isEmailActive() == false) {
            throw new UsernameNotFoundException("Tài khoản chưa được kích hoạt");
        }
        RequestContext context = RequestContext.get();
        context.setUserId(userEntity.getUserId());

        RoleEntity roleEntity = roleRepository.findById(userEntity.getRoleId().getId())
                .orElseThrow(() -> new UsernameNotFoundException("Không có quyền hạn này"));
        context.setRole("ROLE_"+roleEntity.getName());
        return User.withUsername(userEntity.getEmail())
                .password(userEntity.getPassword())
                .roles(roleEntity.getName())
                .build();
    }
}