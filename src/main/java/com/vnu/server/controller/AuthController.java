package com.vnu.server.controller;

import com.vnu.server.entity.Role;
import com.vnu.server.entity.User;
import com.vnu.server.jwt.JwtTokenProvider;
import com.vnu.server.model.JwtResponse;
import com.vnu.server.model.MessageResponse;
import com.vnu.server.model.MyUserDetails;
import com.vnu.server.repository.RoleRepository;
import com.vnu.server.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class  AuthController {
    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
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
    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        List<String> roles = myUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                myUserDetails.getUser().getUsername(),
                myUserDetails.getUser().getFullName(),
                roles));
    }
    @Data
    private static class AuthRequest{
        private String username;
        private String password;
    }
}
