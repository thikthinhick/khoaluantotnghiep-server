package com.vnu.server.service.statistic;

import java.util.List;
import java.util.Map;

public interface StatisticService {
    Long getTotalConsumptionDay(String day, Long applianceId);
    Long getTotalConsumptionDay(String day);
    Long getTotalConsumptionMonth(String month, Long applianceId);
    Long getTotalConsumptionMonth(String month);
    Long getTotalConsumption(Long applianceId);
    Long getTotalConsumption();
    Long getTotalConsumptionByRoom(String date, Long roomId);
    Double getPrice(String day, Long applianceId);
    Double getPrice(String day);
    List<Integer> getConsumptionMostRecentDays(String day);
    List<Integer> getConsumptionPerHour(String day, int type);
    Map<String, Integer> getConsumptionByRooms(String day, int type);
    Map<String, String> getConsumptionByAppliances(String day, int type);
}
