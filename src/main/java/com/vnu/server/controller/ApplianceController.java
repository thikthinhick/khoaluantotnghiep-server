package com.vnu.server.controller;

import com.vnu.server.entity.Appliance;
import com.vnu.server.model.MessageResponse;
import com.vnu.server.service.appliance.ApplianceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/appliance")
@Slf4j
@RequiredArgsConstructor
public class ApplianceController {
    private final ApplianceService applianceService;
    @PostMapping
    public ResponseEntity<?> saveAppliance(@RequestBody Appliance appliance, HttpServletRequest request) {
        applianceService.save(appliance);
        return ResponseEntity.ok().body(new MessageResponse("Tạo thiết bị thành công!"));
    }
//    @Data
//    @AllArgsConstructor
//    private static class MessageAppliance{
//        private String message;
//        private List<Appliance> appliances;
//    }

}
