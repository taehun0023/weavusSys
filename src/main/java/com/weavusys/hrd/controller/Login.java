package com.weavusys.hrd.controller;

import com.weavusys.hrd.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class Login {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

        @PostMapping("/login")
        public ResponseEntity<?> login (@RequestBody Map < String, String > superAccount){
            String username = superAccount.get("username");
            String password = superAccount.get("password");
            try {
                // 슈퍼 계정 인증 처리
                if ("admin".equals(username) && "1234".equals(password)) {
                    Authentication authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(username, password)
                    );

                    // 인증 성공 시 JWT 토큰 생성
                    String token = jwtTokenProvider.generateToken(authentication);

                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "슈퍼 계정 로그인 성공");
                    response.put("token", token);
                    response.put("success", true);

                    return ResponseEntity.ok(response);
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(Map.of("message", "슈퍼 계정의 아이디 또는 비밀번호가 올바르지 않습니다."));
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "로그인 실패: " + e.getMessage()));
            }
        }
}
