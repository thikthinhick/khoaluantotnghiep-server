package com.vnu.server.controller;

import com.vnu.server.common.FunctionPopular;
import com.vnu.server.entity.Appliance;
import com.vnu.server.jwt.JwtTokenProvider;
import com.vnu.server.model.MessageResponse;
import com.vnu.server.service.appliance.ApplianceService;
import lombok.Data;
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
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<?> saveAppliance(@RequestBody ApplianceRequest applianceRequest, HttpServletRequest request) {
        String jwt = FunctionPopular.getJwtFromRequest(request);
        String userId = jwtTokenProvider.getUserIdFromJWT(jwt);
        applianceService.save(applianceRequest.getAppliance(), Long.parseLong(userId), applianceRequest.getRoomId());
        return ResponseEntity.ok().body(new MessageResponse("Tạo thiết bị thành công!"));
    }

    //    @Data
//    @AllArgsConstructor
//    private static class MessageAppliance{
//        private String message;
//        private List<Appliance> appliances;
//    }
    @Data
    private static class ApplianceRequest {
        private Long roomId;
        private Appliance appliance;
    }

}
