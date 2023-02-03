package com.vnu.server;

import com.vnu.server.entity.Role;
import com.vnu.server.repository.RoleRepository;
import com.vnu.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import static com.vnu.server.entity.Role.RoleName.READ;
import static com.vnu.server.entity.Role.RoleName.USER;

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
        Role role = new Role();
        role.setName(USER.name());
        Role role1 = new Role();
        role1.setName(READ.name());
        roleRepository.save(role);
        roleRepository.save(role1);
    }
}
