package com.vnu.server.service.schedule;

import com.vnu.server.entity.Appliance;
import com.vnu.server.entity.DbSchedule;
import com.vnu.server.exception.ResourceNotFoundException;
import com.vnu.server.repository.ApplianceRepository;
import com.vnu.server.repository.ScheduleRepository;
import com.vnu.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService{
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    private final ApplianceRepository applianceRepository;
    @Override
    public DbSchedule create(DbSchedule schedule, Long applianceId) {
        Appliance appliance  = applianceRepository.findById(applianceId).orElseThrow(() -> new ResourceNotFoundException("Not found appliance!"));
        schedule.setStatus(true);
        schedule.setAppliance(appliance);
        scheduleRepository.save(schedule);
        return schedule;
    }

    @Override
    public void delete(Long userId, Long scheduleId) {
        if(!userRepository.existsById(userId)) throw new ResourceNotFoundException("Not found user!");
        scheduleRepository.deleteById(scheduleId);
    }

    @Override
    public void update(DbSchedule schedule) {
        System.out.println(schedule.toString());
       DbSchedule dbSchedule = scheduleRepository.findById(schedule.getId()).orElseThrow(() -> new ResourceNotFoundException("Not found schedule"));
       dbSchedule.setEndDate(schedule.getEndDate());
       dbSchedule.setStartDate(schedule.getStartDate());
       dbSchedule.setName(schedule.getName());
       dbSchedule.setRepeatDay(schedule.getRepeatDay());
       scheduleRepository.save(dbSchedule);
    }
}
