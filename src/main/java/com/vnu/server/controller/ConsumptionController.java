package com.vnu.server.controller;

import com.vnu.server.model.DataConsumption;
import com.vnu.server.service.ConsumptionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consumption")
@AllArgsConstructor
@CrossOrigin
public class ConsumptionController {
    private final ConsumptionService consumptionService;
    @GetMapping("/last_consumption")
    public List<DataConsumption> getLastConsumption(@RequestParam("type") String type) {
        return consumptionService.getLastConsumption(type);
    }
}
