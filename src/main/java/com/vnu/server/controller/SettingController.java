package com.vnu.server.controller;

import com.vnu.server.entity.User;
import com.vnu.server.repository.BillRepository;
import com.vnu.server.repository.StaffRepository;
import com.vnu.server.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/setting")
@AllArgsConstructor
@CrossOrigin
public class SettingController {
    private final UserRepository userRepository;
    private final BillRepository billRepository;
    @GetMapping
    public ResponseEntity<?> getSeSetting() {
        String result = billRepository.spBillCurrent();
        List<User> users = userRepository.findUsersNotAdmin();
        users.forEach(element -> {
            element.setListRoomNames(element.getMembers().stream().map(item -> item.getRoom().getName()).collect(Collectors.toList()));
            element.setMembers(null);
        });

        SettingResponse settingResponse = SettingResponse.builder()
                .users(users)
                .staffType(result.split(",")[0])
                .staffTypeChange(result.split(",")[1])
                .build();
        return ResponseEntity.ok().body(settingResponse);
    }
    @Data
    @Builder
    private static class SettingResponse{
        private List<User> users;
        private String staffType;
        private String staffTypeChange;
    }
}
