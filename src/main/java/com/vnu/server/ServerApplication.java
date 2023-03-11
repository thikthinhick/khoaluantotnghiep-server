package com.vnu.server;

import com.vnu.server.repository.ConsumptionDayRepository;
import com.vnu.server.repository.ConsumptionRepository;
import com.vnu.server.repository.RoleRepository;
import com.vnu.server.service.statistic.StatisticService;
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
    private final ConsumptionDayRepository consumptionDayRepository;
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws InterruptedException {

    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Test{
        List<Integer> userIds = new ArrayList<>();
    }


}
