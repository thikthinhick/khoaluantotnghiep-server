package com.vnu.server.service;

import com.vnu.server.repository.StaffRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class StaffService {
    private final StaffRepository staffRepository;
    @Transactional
    public void updateStaffHome(Long id) {
        staffRepository.updateStaffHome(id);
    }
}
