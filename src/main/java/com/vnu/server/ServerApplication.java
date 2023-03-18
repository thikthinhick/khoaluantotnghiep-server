package com.vnu.server;

import com.vnu.server.repository.ApplianceRepository;
import com.vnu.server.repository.ConsumptionDayRepository;
import com.vnu.server.repository.ConsumptionRepository;
import com.vnu.server.repository.RoleRepository;
import com.vnu.server.service.statistic.StatisticService;
import com.vnu.server.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.List;


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
        System.out.println(applianceRepository.getAppliancesSearchByKeyword("%u%"));
    }

}
