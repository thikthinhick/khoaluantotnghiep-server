package com.vnu.server.repository;

import com.vnu.server.entity.Appliance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplianceRepository extends JpaRepository<Appliance, Long> {
    Boolean existsApplianceById(Long applianceId);
    @Query(value = "select * from appliance where name like ?1", nativeQuery = true)
    List<Appliance> getAppliancesSearchByKeyword(String keyword);
}
