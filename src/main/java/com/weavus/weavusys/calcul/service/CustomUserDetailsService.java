package com.weavus.weavusys.calcul.service;

import com.weavus.weavusys.calcul.entity.AdminUser;
import com.weavus.weavusys.calcul.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자 검색 로직 구현 (데이터베이스 조회로 변경)
        AdminUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));
        return User.withUsername(user.getUsername())
                .password("{noop}" + user.getPassword())
                .roles(user.getRoles())
                .build();
    }
}
