package com.vnu.server.controller;

import com.vnu.server.entity.Role;
import com.vnu.server.entity.User;
import com.vnu.server.model.MessageResponse;
import com.vnu.server.repository.RoleRepository;
import com.vnu.server.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.aspectj.bridge.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.beans.Encoder;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest auth){
        if(userRepository.existsByUsername(auth.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username đã tồn tại!"));
        }
        User user = User.builder()
                .username(auth.getUsername())
                .password(passwordEncoder.encode(auth.getPassword()))
                .build();
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Role không được tìm thấy!"));
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok().body(new MessageResponse("Tạo tài khoản thành công!"));
    }
    @GetMapping("/test")
    public String Test(){
        return "Hello world";
    }
    @Data
    private static class AuthRequest{
        private String username;
        private String password;
    }
}
