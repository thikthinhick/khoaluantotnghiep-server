package com.vnu.server.repository;

import com.vnu.server.entity.Consumption;
import com.vnu.server.model.CSModel;
import com.vnu.server.model.DataConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public interface ConsumptionRepository extends JpaRepository<Consumption, Long> {
        //insert into consumption_day (appliance_id, consumption_day_time, total_value, price) SELECT appliance_id, DATE_FORMAT(DATE_ADD(now(insert into consumption_day (appliance_id, consumption_day_time, total_value, price) SELECT appliance_id, DATE_FORMAT(DATE_ADD(now(), INTERVAL -30 MINUTE), '%Y-%m-%d') as consumption_day_time, sum(c.current_value) as total_value,  sum(c.current_value * s.price / 3600000) as price FROM consumption c inner join staff s on c.time_bands = s.id where SUBSTRING(c.consumption_time, 1, 10) = DATE_FORMAT(DATE_ADD(now(), INTERVAL -30 MINUTE), '%Y-%m-%d')
    @Query(value = "select consumption_time as time, sum(current_value) / 15 as value from consumption where consumption_time >= DATE_ADD(now(), INTERVAL -15 MINUTE) && consumption_time <= now() group by consumption_time order by consumption_time desc", nativeQuery = true)
    List<CSModel> getLatestConsumptionSecond();
    @Query(value = "select sum(current_value) / 1200 as value, get_floor_time(consumption_time, 20) as time from consumption where timestampdiff(minute,consumption_time, now()) <= 1440 group by time", nativeQuery = true)
    List<CSModel> getLatestConsumptionMinute();
    @Query(value = "delete from consumption where appliance_id = ?1", nativeQuery = true)
    @Modifying
    void deleteByApplianceId(Long id);
    @Query(value = "select IFNULL(sum(current_value), 0) from consumption where consumption_time like ?1 and appliance_id = ?2", nativeQuery = true)
    Long getTotalConsumptionDay(String time, Long applianceId);
    @Query(value = "select IFNULL(sum(current_value), 0) from consumption where consumption_time like ?1", nativeQuery = true)
    Long getTotalConsumptionDate(String time);
    @Query(value = "SELECT IFNULL(sum(current_value), 0)  FROM consumption c inner join appliance a on c.appliance_id = a.id where c.consumption_time like ?1 and a.room_id = ?2", nativeQuery = true)
    Long getConsumptionByRoom(String date, Long roomId);
    @Query(value = "SELECT IFNULL(sum(price * current_value), 0) / 3600000 as total from consumption c inner join staff s on c.time_bands = s.id where consumption_time like ?1", nativeQuery = true)
    Double getPrice(String day);
    @Query(value = "SELECT IFNULL(sum(price * current_value), 0) / 3600000 as total from consumption c inner join staff s on c.time_bands = s.id where consumption_time like ?1 and  c.appliance_id = ?2", nativeQuery = true)
    Double getPrice(String day, Long applianceId);
    @Query(value = "SELECT substr(consumption_time, 1, 7) as time, get_staff_id(substr(consumption_time, 1, 7)) as staff_id, sum(current_value) as total_value, sum(c.current_value * s.price / 3600000) as total_price FROM consumption c inner join staff s on c.time_bands = s.id group by time", nativeQuery = true)
    List<CSModel> getBillAll();
    @Query(value = "SELECT sum(current_value) as value, substr(consumption_time, 12, 2) as time from consumption where consumption_time like ?1 group by substr(consumption_time, 12, 2)", nativeQuery = true)
    List<CSModel> getConsumptionPerHour(String day);
    @Query(value = "SELECT r.name, sum(c.current_value) as value FROM consumption c inner join appliance a on c.appliance_id = a.id inner join room r on a.room_id = r.id where consumption_time like ?1 group by a.room_id", nativeQuery = true)
    List<CSModel> getConsumptionByRooms(String day);
    @Query(value = "SELECT a.name as name, sum(current_value) as value FROM khoaluantotnghiep.consumption c inner join appliance a on c.appliance_id = a.id where c.consumption_time like ?1 group by a.name;", nativeQuery = true)
    List<CSModel> getConsumptionByAppliances(String day);
    @Query(value = "select substring(consumption_time, 1, 10) as time, sum(current_value) as value from consumption where substring(consumption_time, 1, 10) in ?1 group by substring(consumption_time, 1, 10)", nativeQuery = true)
    List<CSModel> getConsumptionMostRecentDays(Set<String> days);
}
