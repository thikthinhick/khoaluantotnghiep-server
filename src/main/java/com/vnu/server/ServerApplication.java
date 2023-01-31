package com.vnu.server;

import com.vnu.server.entity.Role;
import com.vnu.server.entity.User;
import com.vnu.server.repository.RoleRepository;
import com.vnu.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class ServerApplication implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Role role = Role.builder()
                        .name("ADMIN")
                                .build();
        roleRepository.save(role);
        User user = User.builder()
                .username("chuong")
                .email("chuong2001@gmail.com")
                .password("chuong2001")
                .thumbnail("http://google.com.vn")
                .build();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
    }
}
