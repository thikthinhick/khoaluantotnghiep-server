package com.vnu.server.controller;

import com.vnu.server.entity.Member;
import com.vnu.server.entity.Role;
import com.vnu.server.entity.User;
import com.vnu.server.exception.ResourceNotFoundException;
import com.vnu.server.jwt.JwtTokenProvider;
import com.vnu.server.model.JwtResponse;
import com.vnu.server.model.MessageResponse;
import com.vnu.server.model.MyUserDetails;
import com.vnu.server.repository.RoleRepository;
import com.vnu.server.repository.RoomRepository;
import com.vnu.server.repository.TokenRepository;
import com.vnu.server.repository.UserRepository;
import com.vnu.server.service.EmailSender;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class AuthController {
    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    private final RoleRepository roleRepository;
    private final RoomRepository roomRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailSender emailSender;
    private final TokenRepository tokenRepository;


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest authRequest) {
        if (userRepository.existsByUsername(authRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username sử dụng đã tồn tại!"));
        }
        if (userRepository.existsByEmail(authRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email sử dụng đã tồn tại"));
        }
        User user = User.builder()
                .username(authRequest.getUsername())
                .password(passwordEncoder.encode(authRequest.getPassword()))
                .build();
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Role không được tìm thấy!"));
        roles.add(role);
        user.setRoles(roles);
        user.setEmail(authRequest.getEmail());
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        tokenRepository.generateToken(authRequest.getEmail().concat("SIGNUP"), user);
        try {
            emailSender.sendMail(authRequest.getEmail(), "XÁC NHẬN TẠO TÀI KHOẢN", "http://localhost:8081/api/auth/confirm_create_account?email=" + authRequest.getEmail() + "&token=" + token);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body(new MessageResponse("Kiểm tra email để xác nhận tạo tài khoản!"));
    }

    @GetMapping("/confirm_create_account")
    public ResponseEntity<?> confirmCreateAccount(@RequestParam("email") String email, @RequestParam("token") String token) {
        if (tokenRepository.get(email.concat("SIGNUP")) == null) {
            log.error("Token không tồn tại");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Không thể xác nhận tạo tài khoản"));
        }
        User user = tokenRepository.get(email.concat("SIGNUP"));
        if (!user.getToken().equals(token)) {
            log.error("Token không thõa mãn");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Không thể xác nhận tạo tài khoản"));
        }
        tokenRepository.remove(email.concat("SIGNUP"));
        userRepository.save(user);
        return ResponseEntity.ok().body("Tạo tài khoản thành công!");
    }

    @PostMapping("/forgot_password")
    public ResponseEntity<?> forgotPassword(@RequestBody AuthRequest authRequest) {
        if (!userRepository.existsByEmail(authRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Email không tồn tại!");
        }
        String token = UUID.randomUUID().toString();
        User user = new User();
        user.setEmail(authRequest.getEmail());
        tokenRepository.generateToken(token, user);
        try {
            emailSender.sendMail(authRequest.getEmail(), "XÁC THỰC CẬP NHẬT LẠI MẬT KHẨU", "http://localhost:3000/reset-password/".concat(token));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body("Kiểm tra email để reset mật khẩu!");
    }

    @GetMapping("/confirm_reset_password/{token}")
    public ResponseEntity<?> confirmResetPassword(@PathVariable("token") String token) {
        User user = tokenRepository.get(token);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Không thể xác thực cập nhật mật khẩu");
        }
        String access_token_reset = UUID.randomUUID().toString();
        tokenRepository.generateToken(access_token_reset, user);
        tokenRepository.remove(token);
        return ResponseEntity.ok().body(new MessageResponse<String>("Lấy dữ liệu thành công", access_token_reset));
    }

    @PutMapping("/reset_password/{token}")
    public ResponseEntity<?> resetPassword(@PathVariable("token") String token, @RequestBody User authRequest) {
        User info = tokenRepository.get(token);
        if (info == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token không chính xác");
        }
        ;
        User user = userRepository.findUserByEmail(info.getEmail());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        userRepository.save(user);
        tokenRepository.remove(token);
        return ResponseEntity.ok().body(new MessageResponse<>("Cập nhật mật khẩu thành công"));
    }

    @PutMapping("/update_password")
    public ResponseEntity<?> updatePassword(@RequestBody User authRequest) {
        User user = userRepository.findUserByEmail(authRequest.getEmail());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok().body(new MessageResponse<>("Cập nhật mật khẩu thành công"));
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
                myUserDetails.getUser().getId(),
                myUserDetails.getUser().getThumbnail(),
                roles));
    }

    @GetMapping("/get_user")
    public ResponseEntity<?> getUser(@RequestParam("room_id") Long roomId) {
        List<User> users = userRepository.findUsersNotAdmin();
        List<Long> ids = roomRepository.getById(roomId)
                .getMembers().stream()
                .map(element -> element.getUser().getId()).collect(Collectors.toList());
        List<UserResponse> userResponses = users.stream().map(element ->
                UserResponse.builder()
                        .username(element.getUsername())
                        .id(element.getId())
                        .thumbnail(element.getThumbnail())
                        .checked(ids.contains(element.getId()))
                        .build()
        ).collect(Collectors.toList());
        return ResponseEntity.ok().body(userResponses);
    }

    @Data
    private static class AuthRequest {
        private String username;
        private String password;
        private String email;
    }

    @Data
    @Builder
    private static class UserResponse {
        private Long id;
        private String username;
        private String thumbnail;
        private boolean checked;
    }
}
