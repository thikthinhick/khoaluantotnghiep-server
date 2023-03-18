package com.vnu.server.repository;

import com.vnu.server.entity.DbSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<DbSchedule, Long> {
    @Modifying
    void deleteDbScheduleByApplianceId(Long applianceId);
    List<DbSchedule> findDbScheduleByStatus(Boolean status);
}
