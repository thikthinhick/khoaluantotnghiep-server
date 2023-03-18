package com.vnu.server.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import com.vnu.server.entity.Appliance;
import com.vnu.server.exception.ResourceNotFoundException;
import com.vnu.server.model.MessageResponse;
import com.vnu.server.model.RequestData;
import com.vnu.server.repository.ApplianceRepository;
import com.vnu.server.service.appliance.ApplianceService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

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
        applianceService.delete(applianceRequest.getApplianceId());
        return ResponseEntity.ok().body(new MessageResponse<>("Xoa thiet bi thanh cong!"));
    }

    @GetMapping
    public ResponseEntity<?> getAppliance(@RequestParam(name = "id", required = false) Long applianceId) {
        if (applianceId == null) return ResponseEntity.ok().body(applianceRepository.findAll());
        return ResponseEntity.ok().body(applianceRepository.findById(applianceId).orElseThrow(() -> new ResourceNotFoundException("not found appliance")));
    }

    @PutMapping("/change_status")
    public ResponseEntity<?> changeStatusAppliance(@RequestBody ApplianceRequest applianceRequest, @RequestParam("id") Long id) {
        if (!applianceRepository.existsApplianceById(id))
            throw new ResourceNotFoundException("Không tìm thấy thiết bị!");
        try {
            final String uri = "http://localhost:8080/api/appliance?id=" + id;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<?> responseEntity = restTemplate.postForEntity(uri, applianceRequest, Object.class);
            if(responseEntity.getStatusCode() == HttpStatus.OK) {
                log.info("Call api change status to server hub");
                return ResponseEntity.ok().body(new MessageResponse<>("Cập nhật trạng thái thiết bị thành công!", responseEntity.getBody()));
            }
            return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Không thể kết nối tới hub!");
        }
    }
    @GetMapping("/search")
    public ResponseEntity<?> searchAppliance(@RequestParam("keyword") String keyword) {
        List<Appliance> applianceList = applianceRepository.getAppliancesSearchByKeyword("%" + keyword + "%");
        List<ApplianceRequest> appliances = applianceList.stream().map(element ->
            ApplianceRequest.builder()
                    .applianceId(element.getId())
                    .applianceName(element.getName())
                    .roomId(element.getRoom().getId())
                    .roomName(element.getRoom().getName())
                    .build()
        ).collect(Collectors.toList());
        return ResponseEntity.ok().body(new MessageResponse<>("Lấy thông tin thành công!", appliances));
    }

    @Data
    @ToString
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class ApplianceRequest {
        private Long userId;
        private Long applianceId;
        private String applianceName;
        private Long roomId;
        private String roomName;
        private Boolean status;
    }

}
