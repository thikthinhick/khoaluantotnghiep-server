package com.vnu.server.controller;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/statistic")
@CrossOrigin
public class StatisticController {
    public ResponseEntity<?> getStatistic() {
        StatisticResponse statisticResponse = new StatisticResponse();
        return null;
    }
    @Data
    private static class StatisticResponse{
        List<BillStaff> billStaffs = new ArrayList<>();
    }
    @Data
    private static class BillStaff{
        private String month;
        private String year;
        private Integer totalDay;
        private String consumption;
        private String staffType;
        private String cost;
    }
}
