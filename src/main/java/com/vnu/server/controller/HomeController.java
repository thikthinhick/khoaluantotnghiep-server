package com.vnu.server.controller;

import com.vnu.server.model.DataResponse;
import com.vnu.server.service.statistic.StatisticService;
import com.vnu.server.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@CrossOrigin
@RequestMapping("/api/home")
@AllArgsConstructor
public class HomeController {
    private final StatisticService statisticService;
    @GetMapping
    public ResponseEntity<?> getData() {
        Date date = new Date();
        String day = StringUtils.convertDateToString(date, "yyyy-MM-dd");
        String month = StringUtils.convertDateToString(date, "yyyy-MM");
        Long consumptionInDay = statisticService.getTotalConsumptionDay(day);
        Long consumptionInMonth = statisticService.getTotalConsumptionMonth(StringUtils.convertDateToString(date, "yyyy-MM"));
        Long consumptionTotal = statisticService.getTotalConsumption();
        int costLastMonth = (int) Math.round(statisticService.getPrice(StringUtils.lastOneMonth(date)));
        int costCurrentMonth = (int)Math.round(statisticService.getPrice(month));
        int costTotal = (int) Math.round(statisticService.getPrice(""));
        DataResponse dataResponse = DataResponse.builder()
                .consumptionInDay(StringUtils.convertJunToNumber(consumptionInDay))
                .consumptionInMonth(StringUtils.convertJunToNumber(consumptionInMonth))
                .consumptionInYear(StringUtils.convertJunToNumber(consumptionInMonth))
                .consumptionTotal(StringUtils.convertJunToNumber(consumptionTotal))
                .costLastMonth(StringUtils.convertNumberToCost(costLastMonth))
                .costCurrentMonth(StringUtils.convertNumberToCost(costCurrentMonth))
                .costTotal(StringUtils.convertNumberToCost(costTotal))
                .build();
        return ResponseEntity.ok(dataResponse);
    }
}
