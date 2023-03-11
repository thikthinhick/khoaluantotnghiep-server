package com.vnu.server.service.appliance;

import com.vnu.server.entity.Appliance;
import com.vnu.server.model.RequestData;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ApplianceService {
    void save(Appliance appliance, Long userId, Long roomId);
    void update(Appliance appliance);
    void delete(Long applianceId);
    Appliance save(MultipartFile multipartFile, RequestData requestData);
    List<Appliance> getAll();
    Appliance getById(Long applianceId);
    void removeAppliance(Long userId, Long applianceId);
}
