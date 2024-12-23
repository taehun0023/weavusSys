package com.weavusys.hrd.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자 검색 로직 구현 (예: 데이터베이스 조회)
        if ("admin".equals(username)) {
            return User.withUsername("admin")
                    .password("{noop}1234") // 비밀번호는 인코딩된 상태로 제공해야 함
                    .roles("ADMIN")
                    .build();
        }
        throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
    }
}
