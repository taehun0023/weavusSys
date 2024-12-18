//package com.weavusys.hrd.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (필요시 활성화)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/", "/login", "/register").permitAll() // 로그인, 회원가입 페이지는 접근 허용
//                        .anyRequest().authenticated() // 그 외 페이지는 인증 필요
//                )
//                .formLogin(form -> form
//                        .loginPage("/login") // 커스텀 로그인 페이지
//                        .defaultSuccessUrl("/home", true) // 로그인 성공 시 리다이렉트
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/login")
//                        .permitAll()
//                );
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
