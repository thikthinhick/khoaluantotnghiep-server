package com.vnu.server.service.appliance;

import com.vnu.server.entity.Appliance;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ApplianceService {
    void save(Appliance appliance, HttpServletRequest request);
    void update(Appliance appliance);
    List<Appliance> getAll();
    Appliance getById(Long applianceId);
    void removeApplianceToRoom(Long roomId, Long applianceId);
    void addApplianceToRoom(Long roomId, Long applianceId);
}
