package com.vnu.server.service.schedule;

import com.vnu.server.entity.DbSchedule;

public interface ScheduleService {
    void create(DbSchedule schedule, Long applianceId);
    void delete(Long userId, Long scheduleId);
    void update(DbSchedule schedule);
}
