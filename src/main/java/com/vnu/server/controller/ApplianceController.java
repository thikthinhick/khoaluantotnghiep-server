package com.vnu.server.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import com.vnu.server.entity.Appliance;
import com.vnu.server.exception.ResourceNotFoundException;
import com.vnu.server.model.MessageResponse;
import com.vnu.server.model.RequestData;
import com.vnu.server.repository.ApplianceRepository;
import com.vnu.server.repository.RoomRepository;
import com.vnu.server.service.appliance.ApplianceService;
import com.vnu.server.service.notification.NotificationService;
import com.vnu.server.service.statistic.StatisticService;
import com.vnu.server.utils.StringUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.PermitAll;
import java.util.Date;
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
    private final RoomRepository roomRepository;
    private final StatisticService statisticService;
    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<?> createAppliance(@RequestParam(required = false, name = "file") MultipartFile multipartFile, @RequestParam("data") String data) {
        Gson gson = new Gson();
        RequestData requestData = gson.fromJson(data, RequestData.class);
        Appliance appliance = applianceService.save(multipartFile, requestData);
        try {
            final String uri = "http://localhost:8080/api/appliance/add_appliance?id=" + appliance.getId();
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<?> responseEntity = restTemplate.getForEntity(uri, Object.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                log.info("Call api change status to server hub");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("Chưa thêm được thiết bị vào hub vui lòng reset lại hub!");
        }
        return ResponseEntity.ok().body(new MessageResponse<>("Tạo thiết bị thành công!", appliance));
    }
    @PutMapping
    public ResponseEntity<?> updateAppliance(@RequestParam(required = false, name = "file") MultipartFile multipartFile, @RequestParam("data") String data) {
        Gson gson = new Gson();
        RequestData requestData = gson.fromJson(data, RequestData.class);
        applianceService.update(multipartFile, requestData);
        return ResponseEntity.ok().body(new MessageResponse<>("Chỉnh sửa thiết bị thành công!"));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAppliance(@RequestBody ApplianceRequest applianceRequest) {
        applianceService.delete(applianceRequest.getApplianceId());
        return ResponseEntity.ok().body(new MessageResponse<>("Xoa thiet bi thanh cong!"));
    }

    @GetMapping
    public ResponseEntity<?> getAppliance(@RequestParam(name = "id", required = false) Long applianceId) {
        Date date = new Date();
        String month = StringUtils.convertDateToString(date, "yyyy-MM");
        if (applianceId == null) return ResponseEntity.ok().body(applianceRepository.findAll());
        Appliance appliance = applianceRepository.findById(applianceId).orElseThrow(() -> new ResourceNotFoundException("not found appliance"));
        Long consumptionCurrentMonth = statisticService.getTotalConsumptionMonth(month, applianceId);
        Long consumptionTotal = statisticService.getTotalConsumption(applianceId);
        int costCurrentMonth = (int) Math.round(statisticService.getPrice(month, applianceId));
        int costTotal = (int) Math.round(statisticService.getPrice("", applianceId));
        ApplianceRequest applianceRequest = new ApplianceRequest();
        applianceRequest.setAppliance(appliance);
        applianceRequest.setConsumptionTotal(StringUtils.convertJunToNumber(consumptionTotal));
        applianceRequest.setConsumptionCurrentMonth(StringUtils.convertJunToNumber(consumptionCurrentMonth));
        applianceRequest.setCostTotal(StringUtils.convertNumberToCost(costTotal));
        applianceRequest.setCostCurrentMonth(StringUtils.convertNumberToCost(costCurrentMonth));
        return ResponseEntity.ok().body(applianceRequest);
    }
    @PutMapping("/change_status")
    public ResponseEntity<?> changeStatusAppliance(@RequestBody ApplianceRequest applianceRequest) {

        if (!applianceRepository.existsApplianceById(applianceRequest.getApplianceId()))
            throw new ResourceNotFoundException("Không tìm thấy thiết bị!");
        try {
            final String uri = "http://localhost:8080/api/appliance/change_status";
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<?> responseEntity = restTemplate.postForEntity(uri, applianceRequest, Object.class);
            if(responseEntity.getStatusCode() == HttpStatus.OK) {
                log.info("Call api change status to server hub");
                int notificationType = applianceRequest.getStatus() ? 1 : 2;
                notificationService.createNotification(applianceRequest.getApplianceId(), applianceRequest.getUserId(), notificationType);
                return ResponseEntity.ok().body(new MessageResponse<>("Cập nhật trạng thái thiết bị thành công!"));
            }
            return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Không thể kết nối tới hub!");
        }
    }
    @PutMapping("/change_status_all")
    public ResponseEntity<?> changeStatusAllAppliance(@RequestBody ApplianceRequest applianceRequest) {
        if (!roomRepository.existsRoomById(applianceRequest.getRoomId()))
            throw new ResourceNotFoundException("Không tìm thấy phòng!");
        try {
            final String uri = "http://localhost:8080/api/appliance/change_status_all";
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<?> responseEntity = restTemplate.postForEntity(uri, applianceRequest, Object.class);
            if(responseEntity.getStatusCode() == HttpStatus.OK) {
                log.info("Call api change status to server hub");
//                notificationService.createNotification(applianceRequest.getApplianceId(), applianceRequest.getUserId(), 7);
                return ResponseEntity.ok().body(new MessageResponse<>("Tắt tất cả các thiết bị trong phòng thành công!"));
            }
            return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Không thể kết nối tới hub!");
        }
    }
    @PermitAll
    @GetMapping("/{applianceId}/standby")
    public void receiveNotificationStandby(@PathVariable("applianceId") Long applianceId){
        notificationService.createNotification(applianceId, null, 6);
    }
    @PutMapping("/change_auto_off")
    public ResponseEntity<?> changeAutoOffAppliance(@RequestBody ApplianceRequest applianceRequest) {
        if (!applianceRepository.existsApplianceById(applianceRequest.getApplianceId())) throw new ResourceNotFoundException("Không tìm thấy thiết bị!");
        try {
            final String uri = "http://localhost:8080/api/appliance/change_auto_off";
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<?> responseEntity = restTemplate.postForEntity(uri, applianceRequest, Object.class);
            if(responseEntity.getStatusCode() == HttpStatus.OK) {
                log.info("Call api change auto_off to server hub");
                return ResponseEntity.ok().body(new MessageResponse<>("Cập nhật auto off thiết bị thành công!"));
            }
            return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
        } catch (Exception e) {
            e.printStackTrace();
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
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class ApplianceRequest {
        private Long userId;
        private Long applianceId;
        private String applianceName;
        private Long roomId;
        private String roomName;
        private Boolean status;
        private Boolean autoOff;
        private Appliance appliance;
        private String consumptionCurrentMonth;
        private String consumptionTotal;
        private String costCurrentMonth;
        private String costTotal;
    }
}
