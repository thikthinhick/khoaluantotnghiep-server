package com.vnu.server.service.schedule;

import com.vnu.server.entity.Appliance;
import com.vnu.server.entity.DbSchedule;
import com.vnu.server.exception.ResourceNotFoundException;
import com.vnu.server.model.RequestData;
import com.vnu.server.repository.ApplianceRepository;
import com.vnu.server.repository.ScheduleRepository;
import com.vnu.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    private final ApplianceRepository applianceRepository;

    @Override
    public DbSchedule create(RequestData data) {
        DbSchedule schedule = data.getSchedule();
        if (data.getTypeSchedule()) {
            int start = optimize(schedule.getStartDate(),schedule.getEndDate(), data.getEstimatedTime());
            schedule.setStartDate(convertMinuteToTime(start));
            schedule.setEndDate(convertMinuteToTime(start + data.getEstimatedTime()));
        }
        Appliance appliance = applianceRepository.findById(data.getApplianceId()).orElseThrow(() -> new ResourceNotFoundException("Not found appliance!"));
        schedule.setStatus(true);
        schedule.setAppliance(appliance);
        scheduleRepository.save(schedule);
        return schedule;
    }

    @Override
    public void delete(Long userId, Long scheduleId) {
        if (!userRepository.existsById(userId)) throw new ResourceNotFoundException("Not found user!");
        scheduleRepository.deleteById(scheduleId);
    }

    @Override
    public void update(DbSchedule schedule) {
        DbSchedule dbSchedule = scheduleRepository.findById(schedule.getId()).orElseThrow(() -> new ResourceNotFoundException("Not found schedule"));
        dbSchedule.setEndDate(schedule.getEndDate());
        dbSchedule.setStartDate(schedule.getStartDate());
        dbSchedule.setName(schedule.getName());
        dbSchedule.setRepeatDay(schedule.getRepeatDay());
        scheduleRepository.save(dbSchedule);
    }
    // Thiếu ngày chủ nhật
    public int optimize(String minTime, String maxTime, int estimatedTime) {
        int[] arr = initialArray();
        int[] price = {1100, 1685, 3076};
        int start = convertStringToMinute(minTime);
        int end = convertStringToMinute(maxTime);
        if(end < start) end += 1440;
        int sum = 0;
        for(int i = start; i < start + estimatedTime; i++) {
            sum += price[arr[i]];
        }
        int x = sum;
        int index = start;
        for(int i = start + estimatedTime; i <= end; i++) {
            x = x - price[arr[i - estimatedTime]] + price[arr[i]];
            if(x < sum) {
                sum = x;
                index = i - estimatedTime;
            }
        }
        return index;
    }

    private int[] initialArray() {
        int[] arr = new int[2010];
        for (int i = 0; i < 240; i++) {
            arr[i] = 0;
        }
        for (int i = 240; i < 570; i++) {
            arr[i] = 1;
        }
        for (int i = 570; i < 690; i++) {
            arr[i] = 2;
        }
        for (int i = 690; i < 1020; i++) {
            arr[i] = 1;
        }
        for (int i = 1020; i < 1200; i++) {
            arr[i] = 2;
        }
        for (int i = 1200; i < 1320; i++) {
            arr[i] = 1;
        }
        for (int i = 1320; i < 1440; i++) {
            arr[i] = 0;
        }
        for(int i = 1440; i < 1680; i++) {
            arr[i] = 0;
        }
        for(int i = 1680; i < 2010; i++) {
            arr[i] = 1;
        }
        return arr;
    }

    private int convertStringToMinute(String time) {
        int hour = Integer.parseInt(time.split(":")[0]);
        int minute = Integer.parseInt(time.split(":")[1]);
        return hour * 60 + minute;
    }
    private String convertMinuteToTime(int minute) {
        int div = minute / 60;
        int mod = minute % 60;
        return (div < 10 ? "0" + div : div) + ":" + (mod < 10 ? "0" + mod : mod);
    }
}
