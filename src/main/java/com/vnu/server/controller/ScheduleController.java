package com.vnu.server.controller;

import com.vnu.server.model.MessageResponse;
import com.vnu.server.model.RequestData;
import com.vnu.server.repository.ScheduleRepository;
import com.vnu.server.service.schedule.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final ScheduleRepository scheduleRepository;
    @PostMapping
    ResponseEntity<?> createSchedule(@RequestBody RequestData requestData) {
        scheduleService.create(requestData.getSchedule(), requestData.getApplianceId());
        return ResponseEntity.ok().body(new MessageResponse<>("Create schedule success!"));
    }
    @GetMapping
    ResponseEntity<?> getAllSchedule() {
        return ResponseEntity.ok().body( scheduleRepository.findAll());
    }

}
