package com.vnu.server;

import com.vnu.server.repository.ConsumptionDayRepository;
import com.vnu.server.repository.ConsumptionRepository;
import com.vnu.server.service.statistic.StatisticService;
import com.vnu.server.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Year;
import java.util.Date;
import java.util.Objects;


@SpringBootApplication
@AllArgsConstructor
@EnableScheduling
public class ServerApplication implements CommandLineRunner {

    private final StatisticService statisticService;
    private final ConsumptionRepository consumptionRepository;
    private final ConsumptionDayRepository consumptionDayRepository;
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws InterruptedException {

    }


}
