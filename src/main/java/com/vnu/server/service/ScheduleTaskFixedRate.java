package com.vnu.server.service;

import com.vnu.server.entity.DbSchedule;
import com.vnu.server.exception.ResourceNotFoundException;
import com.vnu.server.model.MessageResponse;
import com.vnu.server.repository.ScheduleRepository;
import com.vnu.server.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class ScheduleTaskFixedRate {
    private final ScheduleRepository scheduleRepository;
    private final static Map<String, String> dayNames = new HashMap<>();
    static {
        dayNames.put("T2", "Monday");
        dayNames.put("T3", "Tuesday");
        dayNames.put("T4", "Wednesday");
        dayNames.put("T5", "Thursday");
        dayNames.put("T6", "Friday");
        dayNames.put("T7", "Saturday");
        dayNames.put("CN", "Sunday");
    }
    @Scheduled(cron = "0 * * * * ?")
    public void reportCurrentTime() {
        Date now = new Date();
        String time = StringUtils.convertDateToString(now, "HH:mm");
        String nameDay = StringUtils.convertDateToString(now, "EEEE");
        List<DbSchedule> schedules = scheduleRepository.findDbScheduleByStatus(true);
        schedules.forEach(element -> {
            if(element.getStartDate().equals(time) && !element.getAppliance().getStatus()&& checkDay(element.getRepeatDay(), nameDay)) {
                changeStatusAppliance(element.getAppliance().getId(), true);
                if(element.getRepeatDay().equals("")) {
                    element.setStatus(false);
                    scheduleRepository.save(element);
                }
            }
            if(element.getEndDate().equals(time) && element.getAppliance().getStatus()){
                changeStatusAppliance(element.getAppliance().getId(), false);
            }
        });
    }
    private boolean checkDay(String repeatDay, String nameDay) {
        if(repeatDay.equals("")) return true;
        for(String element : repeatDay.split(",")) {
            if(dayNames.get(element).equals(nameDay)) return true;
        }
        return false;
    }
    private void changeStatusAppliance(Long applianceId, Boolean status) {
        try {
            final String uri = "http://localhost:8080/api/appliance?id=" + applianceId;
            RestTemplate restTemplate = new RestTemplate();
            RequestSchedule requestSchedule = new RequestSchedule();
            requestSchedule.setStatus(status);
            ResponseEntity<?> responseEntity = restTemplate.postForEntity(uri, requestSchedule, Object.class);
            if(responseEntity.getStatusCode() == HttpStatus.OK) {
                log.info("Call api change status to server hub success");
            }
        } catch (Exception e) {
            log.error("Call api change status to server hub error");
        }
    }
    @Data
    private static class RequestSchedule{
        private Boolean status;
    }
}