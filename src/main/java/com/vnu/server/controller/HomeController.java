package com.vnu.server.controller;

import com.vnu.server.entity.Home;
import com.vnu.server.exception.ResourceNotFoundException;
import com.vnu.server.model.DataResponse;
import com.vnu.server.repository.HomeRepository;
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
    private final HomeRepository homeRepository;
    @GetMapping
    public ResponseEntity<?> getData() {
        Date date = new Date();
        String day = StringUtils.convertDateToString(date, "yyyy-MM-dd");
        String month = StringUtils.convertDateToString(date, "yyyy-MM");
        Long consumptionInDay = statisticService.getTotalConsumptionDay(day);
        Long consumptionInMonth = statisticService.getTotalConsumptionDay(day.substring(0, 7));
        Long consumptionInYear = statisticService.getTotalConsumptionDay(day.substring(0, 4));
        Long consumptionTotal = statisticService.getTotalConsumptionDay("");
        int costLastMonth = (int) Math.round(statisticService.getPrice(StringUtils.lastOneMonth(date)));
        int costCurrentMonth = (int)Math.round(statisticService.getPrice(month));
        int costTotal = (int) Math.round(statisticService.getPrice(""));
        int costCurrentDay = (int) Math.round(statisticService.getPrice(day));
        Home home = homeRepository.findById(1L).orElseThrow(() -> new ResourceNotFoundException("Not find homeId"));
        String staffType = home.getStaffId() == 1L ? "đơn" : (home.getStaffId() == 2L ? "theo thời điểm" : "theo tiêu thụ");
        DataResponse dataResponse = DataResponse.builder()
                .consumptionInDay(StringUtils.convertJunToNumber(consumptionInDay))
                .consumptionInMonth(StringUtils.convertJunToNumber(consumptionInMonth))
                .consumptionInYear(StringUtils.convertJunToNumber(consumptionInYear))
                .consumptionTotal(StringUtils.convertJunToNumber(consumptionTotal))
                .costLastMonth(StringUtils.convertNumberToCost(costLastMonth))
                .costCurrentDay(StringUtils.convertNumberToCost(costCurrentDay))
                .costCurrentMonth(StringUtils.convertNumberToCost(costCurrentMonth))
                .staffType(staffType)
                .costTotal(StringUtils.convertNumberToCost(costTotal))
                .build();
        return ResponseEntity.ok(dataResponse);
    }
}
