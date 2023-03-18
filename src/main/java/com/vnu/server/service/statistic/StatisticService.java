package com.vnu.server.service.statistic;

public interface StatisticService {
    Long getTotalConsumptionDay(String day, Long applianceId);
    Long getTotalConsumptionDay(String day);
    Long getTotalConsumptionMonth(String month, Long applianceId);
    Long getTotalConsumptionMonth(String month);
    Long getTotalConsumptionYear(String year, Long applianceId);
    Long getTotalConsumptionYear(String year);
    Long getTotalConsumption(Long applianceId);
    Long getTotalConsumption();
    Long getTotalConsumptionByRoom(String date, Long roomId);
    Double getPrice(String day, Long applianceId);
    Double getPrice(String day);
}
