package com.vnu.server.repository;

import com.vnu.server.entity.Consumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsumptionRepository extends JpaRepository<Consumption, Long> {
    @Query(value = "select * from consumption where consumption_time >= DATE_ADD(now(), INTERVAL -?1 MINUTE) && consumption_time <= now() order by id desc;", nativeQuery = true)
    public List<Consumption> getLatestConsumption(int distance);
}
