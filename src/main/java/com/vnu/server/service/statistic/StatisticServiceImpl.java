package com.vnu.server.service.statistic;

import com.vnu.server.repository.ConsumptionDayRepository;
import com.vnu.server.repository.ConsumptionRepository;
import com.vnu.server.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class StatisticServiceImpl implements StatisticService{
    private final ConsumptionRepository consumptionRepository;
    private final ConsumptionDayRepository consumptionDayRepository;
    @Override
    public Long getTotalConsumptionDay(String day, Long applianceId) {
        String now = StringUtils.convertDateToString(new Date(), "yyyy-MM-dd");
        if(day.equals(now)) {
            return consumptionRepository.getTotalConsumptionDay(day + "%", applianceId);
        }
        return consumptionDayRepository.getTotalConsumption(day + "%", applianceId);
    }

    @Override
    public Long getTotalConsumptionDay(String day) {
        String now = StringUtils.convertDateToString(new Date(), "yyyy-MM-dd");
        if(day.equals(now)) {
            consumptionDayRepository.getTotalConsumption(day + "%");
        }
        return consumptionRepository.getTotalConsumptionDay(day + "%");
    }

    @Override
    public Long getTotalConsumptionMonth(String month, Long applianceId) {
        String now =  StringUtils.convertDateToString(new Date(), "yyyy-MM-dd");
        return consumptionRepository.getTotalConsumptionDay(now + "%", applianceId)
                + consumptionDayRepository.getTotalConsumption(month + "%", applianceId);
    }

    @Override
    public Long getTotalConsumptionMonth(String month) {
        String now =  StringUtils.convertDateToString(new Date(), "yyyy-MM-dd");
        return consumptionRepository.getTotalConsumptionDay(now + "%")
                + consumptionDayRepository.getTotalConsumption(month + "%");

    }

    @Override
    public Long getTotalConsumptionYear(String year, Long applianceId) {
        String now =  StringUtils.convertDateToString(new Date(), "yyyy-MM-dd");
        return consumptionRepository.getTotalConsumptionDay(now + "%", applianceId)
                + consumptionDayRepository.getTotalConsumption(year + "%", applianceId);
    }

    @Override
    public Long getTotalConsumptionYear(String year) {
        String now =  StringUtils.convertDateToString(new Date(), "yyyy-MM-dd");
        return consumptionRepository.getTotalConsumptionDay(now + "%")
                + consumptionDayRepository.getTotalConsumption(year + "%");
    }

    @Override
    public Long getTotalConsumption(Long applianceId) {
        String now =  StringUtils.convertDateToString(new Date(), "yyyy-MM-dd");
        return consumptionDayRepository.getTotal(applianceId) + consumptionDayRepository.getTotalConsumption(now + "%", applianceId);
    }

    @Override
    public Long getTotalConsumption() {
        String now = StringUtils.convertDateToString(new Date(), "yyyy-MM-dd");
        return consumptionDayRepository.getTotal() + consumptionRepository.getTotalConsumptionDay(now + "%");
    }

    @Override
    public Long getTotalConsumptionByRoom(String date, Long roomId) {
        String now =  StringUtils.convertDateToString(new Date(), "yyyy-MM-dd");
        return consumptionDayRepository.getTotalConsumptionByRoom(date + "%", roomId);
    }

    @Override
    public Double getPrice(String day, Long applianceId) {
        return consumptionDayRepository.getPrice(day + "%", applianceId);
    }

    @Override
    public Double getPrice(String day) {
        return consumptionDayRepository.getPrice(day + "%");
    }
}
