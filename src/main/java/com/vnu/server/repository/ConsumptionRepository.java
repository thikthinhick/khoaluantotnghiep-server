package com.vnu.server.repository;

import com.vnu.server.entity.Consumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsumptionRepository extends JpaRepository<Consumption, Long> {
    //insert into consumption_day (appliance_id, consumption_day_time, total_value, price) SELECT appliance_id, DATE_FORMAT(DATE_ADD(now(), INTERVAL -30 MINUTE), '%Y-%m-%d') as consumption_day_time, sum(c.current_value) as total_value,  sum(c.current_value * s.price / 3600000) as price FROM consumption c inner join staff s on c.time_bands = s.id where SUBSTRING(c.consumption_time, 1, 10) = DATE_FORMAT(DATE_ADD(now(), INTERVAL -30 MINUTE), '%Y-%m-%d')
    @Query(value = "select id, appliance_id, consumption_time, sum(current_value) as current_value, time_bands from consumption where consumption_time >= DATE_ADD(now(), INTERVAL -?1 MINUTE) && consumption_time <= now() group by consumption_time order by consumption_time desc", nativeQuery = true)
    List<Consumption> getLatestConsumption(int distance);
    @Query(value = "delete from consumption where appliance_id = ?1", nativeQuery = true)
    @Modifying
    void deleteByApplianceId(Long id);
    @Query(value = "select IFNULL(sum(current_value), 0) from consumption where consumption_time like ?1 and appliance_id = ?2", nativeQuery = true)
    Long getTotalConsumptionDay(String time, Long applianceId);
    @Query(value = "select IFNULL(sum(current_value), 0) from consumption where consumption_time like ?1", nativeQuery = true)
    Long getTotalConsumptionDay(String time);

}
