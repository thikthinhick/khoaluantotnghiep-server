package com.vnu.server.service.schedule;

import com.vnu.server.entity.DbSchedule;
import com.vnu.server.model.RequestData;

public interface ScheduleService {
    DbSchedule create(RequestData requestData);
    void delete(Long userId, Long scheduleId);
    void update(DbSchedule schedule);
}
