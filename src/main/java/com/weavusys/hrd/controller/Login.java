package com.weavusys.hrd.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class Login {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String , String> superAcount){
        String username = superAcount.get("username");
        String password = superAcount.get("password");


        if ("admin".equals(username) && "1234".equals(password)) {
            String token = generateToken(username); // JWT 토큰 생성 로직
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "로그인 성공");
            response.put("token", token);
            response.put("user", Map.of("id", 1, "username", "admin", "role", "admin"));
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "아이디 또는 비밀번호가 올바르지 않습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    private String generateToken(String username) {
        return "fake-jwt-token-for-" + username; // 실제 JWT 생성은 다른 로직 필요
    }
}
