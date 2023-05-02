package com.vnu.server.service.statistic;

import com.vnu.server.entity.Appliance;
import com.vnu.server.entity.Room;
import com.vnu.server.repository.ApplianceRepository;
import com.vnu.server.repository.ConsumptionDayRepository;
import com.vnu.server.repository.ConsumptionRepository;
import com.vnu.server.repository.RoomRepository;
import com.vnu.server.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final ConsumptionRepository consumptionRepository;
    private final ConsumptionDayRepository consumptionDayRepository;
    private final ApplianceRepository applianceRepository;
    private final RoomRepository roomRepository;

    @Override
    public Long getTotalConsumptionDay(String day, Long applianceId) {
        String now = StringUtils.convertDateToString(new Date(), "yyyy-MM-dd");
        if (day.equals(now)) {
            return consumptionRepository.getTotalConsumptionDay(day + "%", applianceId);
        }
        return consumptionDayRepository.getTotalConsumption(day + "%", applianceId);
    }

    @Override
    public Long getTotalConsumptionDay(String day) {
        return consumptionRepository.getTotalConsumptionDate(day + "%");
    }

    @Override
    public Long getTotalConsumptionMonth(String month, Long applianceId) {
        String now = StringUtils.convertDateToString(new Date(), "yyyy-MM-dd");
        return consumptionRepository.getTotalConsumptionDay(now + "%", applianceId)
                + consumptionDayRepository.getTotalConsumption(month + "%", applianceId);
    }

    @Override
    public Long getTotalConsumptionMonth(String month) {
        String now = StringUtils.convertDateToString(new Date(), "yyyy-MM-dd");
        return consumptionRepository.getTotalConsumptionDate(now + "%")
                + consumptionDayRepository.getTotalConsumption(month + "%");

    }


    @Override
    public Long getTotalConsumption(Long applianceId) {
        String now = StringUtils.convertDateToString(new Date(), "yyyy-MM-dd");
        return consumptionDayRepository.getTotal(applianceId) + consumptionDayRepository.getTotalConsumption(now + "%", applianceId);
    }

    @Override
    public Long getTotalConsumption() {
        String now = StringUtils.convertDateToString(new Date(), "yyyy-MM-dd");
        return consumptionDayRepository.getTotal() + consumptionRepository.getTotalConsumptionDate(now + "%");
    }

    @Override
    public Long getTotalConsumptionByRoom(String date, Long roomId) {
        return consumptionRepository.getConsumptionByRoom(date + "%", roomId);
    }

    @Override
    public Double getPrice(String day, Long applianceId) {
        return consumptionRepository.getPrice(day + "%", applianceId);
    }
    @Override
    public Double getPrice(String day) {
        return consumptionRepository.getPrice(day + "%");
    }

    @Override
    public List<Integer> getConsumptionMostRecentDays(String day) {
        Date date = StringUtils.convertStringDate(day, "yyyy-MM-dd");
        String now = StringUtils.convertDateToString(new Date(), "yyyy-MM-dd");
        int[] distance = {60, 30, 10, 7, 6, 5, 4, 3, 2, 1, 0};
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < 11; i++) {
            String key = StringUtils.convertDateToString(StringUtils.increaseDay(date, -distance[i]), "yyyy-MM-dd");
            map.put(key, 0);
        }

        consumptionRepository.getConsumptionMostRecentDays(map.keySet()).forEach(element -> map.put(element.getTime(), element.getValue() / 3600));
        System.out.println(map);
        return map.keySet().stream().sorted().map(map::get).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getConsumptionPerHour(String day, int type) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < 24; i++) {
            map.put(i, 0);
        }
        switch (type) {
            case 1: {
                consumptionRepository.getConsumptionPerHour(day + "%").forEach(element -> {
                    map.put(Integer.parseInt(element.getTime()), element.getValue());
                });
                break;
            }
            case 2: {
                consumptionRepository.getConsumptionPerHour(day.substring(0, 7) + "%").forEach(element -> {
                    map.put((Integer.parseInt(element.getTime())), element.getValue() / getTotalDayOnMonth(day));
                });
                break;
            }
            case 3: {
                consumptionRepository.getConsumptionPerHour(day.substring(0, 4) + "%").forEach(element -> {
                    map.put(Integer.parseInt(element.getTime()), element.getValue() / getTotalDayOnYear(day));
                });
                break;
            }
            case 4: {
                consumptionRepository.getConsumptionPerHour("%");
                break;
            }
            default:
                break;
        }
        return map.keySet().stream().sorted().map(element -> map.get(element) / 3600).collect(Collectors.toList());
    }

    @Override
    public Map<String, Integer> getConsumptionByRooms(String day, int type) {
        Map<String, Integer> map = new HashMap<>();
        List<Room> rooms = roomRepository.findAll();
        rooms.forEach(element -> map.put(element.getName(), 0));
        switch (type) {
            case 1: {
                consumptionRepository.getConsumptionByRooms(day + "%").forEach(element -> map.put(element.getName(), element.getValue() / 3600));
                break;
            }
            case 2: {
                consumptionRepository.getConsumptionByRooms(day.substring(0, 7) + "%").forEach(element -> map.put(element.getName(), element.getValue() / 3600));
                break;
            }
            case 3: {
                consumptionRepository.getConsumptionByRooms(day.substring(0, 4) + "%").forEach(element -> map.put(element.getName(), element.getValue() / 3600));
                break;
            }
            case 4: {
                consumptionRepository.getConsumptionByRooms("%").forEach(element -> map.put(element.getName(), element.getValue() / 3600));
                break;
            }
            default:
                break;
        }
        return map;
    }

    @Override
    public Map<String, String> getConsumptionByAppliances(String day, int type) {
        Map<String, String> map = new HashMap<>();
        List<Appliance> appliances = applianceRepository.findAll();
        appliances.forEach(element -> map.put(element.getName(), "0"));
        switch (type) {
            case 1: {
                consumptionRepository.getConsumptionByAppliances(day + "%").forEach(element -> map.put(element.getName(), StringUtils.ceilJun(element.getValue())));
                break;
            }
            case 2: {
                consumptionRepository.getConsumptionByAppliances(day.substring(0, 7) + "%").forEach(element -> map.put(element.getName(), StringUtils.ceilJun(element.getValue())));
                break;
            }
            case 3: {
                consumptionRepository.getConsumptionByAppliances(day.substring(0, 4) + "%").forEach(element -> map.put(element.getName(), StringUtils.ceilJun(element.getValue())));
                break;
            }
            case 4: {
                consumptionRepository.getConsumptionByAppliances("%").forEach(element -> map.put(element.getName(), StringUtils.ceilJun(element.getValue())));
                break;
            }
            default:
                break;
        }
        return map;
    }

    private int getTotalDayOnMonth(String date) {
        int year = Integer.parseInt(date.split("-")[0]);
        int month = Integer.parseInt(date.split("-")[1]);
        int day = Integer.parseInt(date.split("-")[2]);
        if (StringUtils.convertDateToString(new Date(), "yyyy-MM").equals(date.substring(0, 7))) return day;
        YearMonth yearMonthObject = YearMonth.of(year, month);
        return yearMonthObject.lengthOfMonth();
    }

    private int getTotalDayOnYear(String date) {
        int year = Integer.parseInt(date.split("-")[0]);
        if (StringUtils.convertDateToString(new Date(), "yyyy").equals(date.substring(0, 4)))
            return (int) StringUtils.getDifferenceDays(Objects.requireNonNull(StringUtils.convertStringDate(year + "-01-01", "yyyy-MM-dd")), new Date());
        Year yearObject = Year.of(year);
        return yearObject.length();
    }
}
