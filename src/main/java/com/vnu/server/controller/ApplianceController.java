package com.vnu.server.controller;

import com.google.gson.Gson;
import com.vnu.server.common.FunctionPopular;
import com.vnu.server.entity.Appliance;
import com.vnu.server.jwt.JwtTokenProvider;
import com.vnu.server.model.MessageResponse;
import com.vnu.server.model.RequestData;
import com.vnu.server.service.appliance.ApplianceService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/appliance")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin
public class ApplianceController {
    private final ApplianceService applianceService;

//    @PostMapping
//    public ResponseEntity<?> saveAppliance(@RequestBody ApplianceRequest applianceRequest, HttpServletRequest request) {
//        String jwt = FunctionPopular.getJwtFromRequest(request);
//        String userId = jwtTokenProvider.getUserIdFromJWT(jwt);
//        applianceService.save(applianceRequest.getAppliance(), Long.parseLong(userId), applianceRequest.getRoomId());
//        return ResponseEntity.ok().body(new MessageResponse("Tạo thiết bị thành công!"));
//    }
    @PostMapping
    public ResponseEntity<?> createAppliance(@RequestParam(required = false, name = "file") MultipartFile multipartFile, @RequestParam("data") String data) {
        Gson gson = new Gson();
        RequestData requestData = gson.fromJson(data, RequestData.class);
        return ResponseEntity.ok().body(new MessageResponse<>("Tạo thiết bị thành công!", applianceService.save(multipartFile, requestData)));
    }
    @DeleteMapping
    public ResponseEntity<?> deleteAppliance(@RequestBody ApplianceRequest applianceRequest) {
        applianceService.removeAppliance(applianceRequest.getUserId(), applianceRequest.getApplianceId());
        return ResponseEntity.ok().body(new MessageResponse<>("Xoa thiet bi thanh cong!"));
    }
    @Data
    @ToString
    private static class ApplianceRequest {
        private Long userId;
        private Long applianceId;
    }

}
