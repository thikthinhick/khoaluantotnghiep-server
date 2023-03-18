package com.vnu.server.repository;

import com.vnu.server.entity.ConsumptionDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumptionDayRepository extends JpaRepository<ConsumptionDay, Long> {
    @Query(value = "select IFNULL(sum(total_value), 0) from consumption_day where consumption_day_time like ?1 and appliance_id = ?2", nativeQuery = true)
    Long getTotalConsumption(String day, Long applianceId);
    @Query(value = "select IFNULL(sum(total_value), 0) from consumption_day where consumption_day_time like ?1", nativeQuery = true)
    Long getTotalConsumption(String day);

    @Query(value = "SELECT IFNULL(sum(total_value), 0)  FROM consumption_day c inner join appliance a on c.appliance_id = a.id where c.consumption_day_time like ?1 and a.room_id = ?2", nativeQuery = true)
    Long getTotalConsumptionByRoom(String date, Long roomId);
    @Query(value = "select IFNULL(sum(total_value), 0) from consumption_day", nativeQuery = true)
    Long getTotal();
    @Query(value = "select IFNULL(sum(total_value), 0) from consumption_day where appliance_id = ?1", nativeQuery = true)
    Long getTotal(Long applianceId);
    @Query(value = "select IFNULL(sum(price), 0) from consumption_day where consumption_day_time like ?1 and appliance_id = ?2", nativeQuery = true)
    Double getPrice(String day, Long appliance_id);
    @Query(value = "select IFNULL(sum(price), 0) from consumption_day where consumption_day_time like ?1", nativeQuery = true)
    Double getPrice(String day);


}
