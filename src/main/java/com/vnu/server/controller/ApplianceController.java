package com.vnu.server.controller;

import com.google.gson.Gson;
import com.vnu.server.exception.ResourceNotFoundException;
import com.vnu.server.model.MessageResponse;
import com.vnu.server.model.RequestData;
import com.vnu.server.repository.ApplianceRepository;
import com.vnu.server.service.appliance.ApplianceService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/appliance")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin
public class ApplianceController {
    private final ApplianceService applianceService;
    private final ApplianceRepository applianceRepository;
    @PostMapping
    public ResponseEntity<?> createAppliance(@RequestParam(required = false, name = "file") MultipartFile multipartFile, @RequestParam("data") String data) {
        Gson gson = new Gson();
        RequestData requestData = gson.fromJson(data, RequestData.class);
        return ResponseEntity.ok().body(new MessageResponse<>("Tạo thiết bị thành công!", applianceService.save(multipartFile, requestData)));
    }
    @DeleteMapping
    public ResponseEntity<?> deleteAppliance(@RequestBody ApplianceRequest applianceRequest) {
//        applianceService.removeAppliance(applianceRequest.getUserId(), applianceRequest.getApplianceId());
        applianceService.delete(applianceRequest.getApplianceId());
        return ResponseEntity.ok().body(new MessageResponse<>("Xoa thiet bi thanh cong!"));
    }
    @GetMapping
    public ResponseEntity<?> getAppliance(@RequestParam(name = "id", required = false) Long applianceId) {
        if(applianceId == null) return ResponseEntity.ok().body(applianceRepository.findAll());
        return ResponseEntity.ok().body(applianceRepository.findById(applianceId).orElseThrow(() -> new ResourceNotFoundException("not found appliance")));
    }
    @Data
    @ToString
    private static class ApplianceRequest {
        private Long userId;
        private Long applianceId;
    }

}
