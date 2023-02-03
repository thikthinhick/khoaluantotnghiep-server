package com.vnu.server.service.appliance;

import com.vnu.server.entity.Appliance;
import com.vnu.server.entity.Room;
import com.vnu.server.exception.ResourceNotFoundException;
import com.vnu.server.repository.ApplianceRepository;
import com.vnu.server.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplianceServiceImpl implements ApplianceService{
    private final ApplianceRepository applianceRepository;
    private final RoomRepository roomRepository;
    @Override
    public void save(Appliance appliance, HttpServletRequest request) {
        String jwt = getJwtFromRequest(request);

        applianceRepository.save(appliance);
    }

    @Override
    public void update(Appliance appliance) {
        if(applianceRepository.existsApplianceById(appliance.getId())) throw new ResourceNotFoundException("khong tim thay thiet bi!");
        applianceRepository.save(appliance);
    }

    @Override
    public List<Appliance> getAll() {
        return null;
    }

    @Override
    public Appliance getById(Long applianceId) {
        return null;
    }

    @Override
    @Transactional
    public void removeApplianceToRoom(Long roomId, Long applianceId) {

    }

    @Override
    public void addApplianceToRoom(Long roomId, Long applianceId) {
        log.info("add appliance to room");
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Khong tim thay phong!"));
        Appliance appliance = applianceRepository.findById(applianceId).orElseThrow(() -> new ResourceNotFoundException("Khong tim thay thiet bi!"));
        appliance.setRoom(room);
        applianceRepository.save(appliance);
    }
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
