package com.vnu.server;

import com.vnu.server.repository.StaffRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@AllArgsConstructor
@EnableScheduling
public class ServerApplication implements CommandLineRunner {

    private final StaffRepository staffRepository;
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws InterruptedException {

    }


}
