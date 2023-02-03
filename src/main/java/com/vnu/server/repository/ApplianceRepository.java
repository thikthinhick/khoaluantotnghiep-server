package com.vnu.server.repository;

import com.vnu.server.entity.Appliance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplianceRepository extends JpaRepository<Appliance, Long> {
    Boolean existsApplianceById(Long applianceId);
}
