package com.vnu.server.controller;

import com.vnu.server.entity.DbSchedule;
import com.vnu.server.exception.ResourceNotFoundException;
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
        DbSchedule dbSchedule = scheduleService.create(requestData);
        return ResponseEntity.ok().body(new MessageResponse<>("Create schedule success!", dbSchedule));
    }
    @PutMapping
    ResponseEntity<?> updateSchedule(@RequestBody RequestData requestData) {
        scheduleService.update(requestData.getSchedule());
        return ResponseEntity.ok().body(new MessageResponse<>("Update schedule success!"));
    }
    @PutMapping("/change_status")
    ResponseEntity<?> updateStatusSchedule(@RequestBody RequestData requestData) {
        DbSchedule schedule = scheduleRepository.findById(requestData.getScheduleId()).orElseThrow(() -> new ResourceNotFoundException("not found schedule"));
        schedule.setStatus(requestData.getScheduleStatus());
        scheduleRepository.save(schedule);
        return ResponseEntity.ok().body(new MessageResponse<>("Update schedule success!"));
    }
    @DeleteMapping
    ResponseEntity<?> deleteSchedule(@RequestBody RequestData requestData) {
        scheduleService.delete(requestData.getUserId(),  requestData.getScheduleId());
        return ResponseEntity.ok().body(new MessageResponse<>("Delete schedule success!"));
    }
    @GetMapping
    ResponseEntity<?> getAllSchedule() {
        return ResponseEntity.ok().body( scheduleRepository.findAll());
    }

}
