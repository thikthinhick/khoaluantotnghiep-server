package com.vnu.server;

import com.vnu.server.repository.ApplianceRepository;
import com.vnu.server.repository.ConsumptionDayRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@AllArgsConstructor
@EnableScheduling
public class ServerApplication implements CommandLineRunner {
    private final ConsumptionDayRepository consumptionDayRepository;
    private final ApplianceRepository applianceRepository;
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws InterruptedException {

    }

}
