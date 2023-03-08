package com.vnu.server;

import com.google.gson.Gson;
import com.vnu.server.repository.RoleRepository;
import com.vnu.server.service.ConsumptionService;
import com.vnu.server.service.FileFirebaseService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
@AllArgsConstructor
public class ServerApplication implements CommandLineRunner {
    private final RoleRepository roleRepository;
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws InterruptedException {
        System.out.println(roleRepository.findByName("READ"));

    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Test{
        List<Integer> userIds = new ArrayList<>();
    }


}
