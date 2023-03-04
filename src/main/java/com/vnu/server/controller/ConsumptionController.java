package com.vnu.server.controller;

import com.vnu.server.model.DataConsumption;
import com.vnu.server.service.ConsumptionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/consumption")
@AllArgsConstructor
@CrossOrigin
public class ConsumptionController {
    private final ConsumptionService consumptionService;
    @GetMapping("/last_consumption")
    public List<DataConsumption> getLastConsumption() {
        return consumptionService.getLastConsumption(15);
    }
}
