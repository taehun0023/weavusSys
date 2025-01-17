package com.weavus.weavusys.calcul.service;

import com.weavus.weavusys.calcul.entity.AdminUser;
import com.weavus.weavusys.calcul.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(String username, String password) {
        //아이디 중복 검사
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("이미 사용중인 아이디입니다.");
        }
        // 사용자 등록 로직 구현 (데이터베이스 저장으로 변경)
        String encodedPassword = passwordEncoder.encode(password);
        AdminUser user = new AdminUser();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setRoles("ADMIN");

        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("회원가입에 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자 검색 로직 구현 (데이터베이스 조회로 변경)
        AdminUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));
        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles())
                .build();
    }
}
