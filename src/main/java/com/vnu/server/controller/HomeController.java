package com.vnu.server.controller;

import com.vnu.server.model.DataResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/home")
public class HomeController {
    @GetMapping
    public ResponseEntity<?> getData() {
        DataResponse dataResponse = DataResponse.builder()
                .consumptionInDay("100.9")
                .consumptionInMonth("2000.2")
                .consumptionInYear("20330.39")
                .consumptionTotal("100020.78")
                .costLastMonth("120.000")
                .costCurrentMonth("52.000")
                .costTotal("1.200.000")
                .build();
        return ResponseEntity.ok(dataResponse);
    }
}
