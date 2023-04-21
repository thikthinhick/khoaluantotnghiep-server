package com.vnu.server.controller;

import com.vnu.server.entity.Staff;
import com.vnu.server.repository.StaffRepository;
import com.vnu.server.service.StaffService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/staff")
@AllArgsConstructor
@CrossOrigin
public class StaffController {
    private final StaffRepository staffRepository;
    private final StaffService staffService;
    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Staff> staffList = staffRepository.findAll();
        Map<String, Staff> data = new HashMap<>();
        staffList.forEach(element -> {
            data.put(element.getName(), element);
        });
        return ResponseEntity.ok().body(data);
    }
    @PutMapping
    public void updateValueStaffAll(@RequestBody Map<String, Staff> map) {
       List<Staff> staffList = new ArrayList<Staff>(map.values());
       staffRepository.saveAll(staffList);
    }
    @PutMapping("/{id}")
    public void changeStaffHome(@PathVariable("id") Long id) {
       staffService.updateStaffHome(id);
    }
}
