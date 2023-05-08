package com.vnu.server.controller;
import com.vnu.server.model.CSModel;
import com.vnu.server.repository.BillRepository;
import com.vnu.server.repository.ConsumptionRepository;
import com.vnu.server.repository.StaffRepository;
import com.vnu.server.service.statistic.StatisticService;
import com.vnu.server.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/statistic")
@CrossOrigin
@AllArgsConstructor
public class StatisticController {
    private final BillRepository billRepository;
    private final ConsumptionRepository consumptionRepository;
    private final StatisticService statisticService;
    private final StaffRepository staffRepository;
    @GetMapping
    public ResponseEntity<?> getStatistic() {
        String now = StringUtils.convertDateToString(new Date(), "yyyy-MM-dd");
        List<Integer> staffs = staffRepository.getStaffSingleAndConsumption();
        List<CSModel> csModels = consumptionRepository.getBillAll();
        List<BillStaff> billStaffs = csModels.stream().map(element -> {
            return BillStaff.builder()
                    .month(element.getTime().split("-")[1])
                    .year(element.getTime().split("-")[0])
                    .consumption(StringUtils.convertJunToNumber(element.getTotalValue()))
                    .staffType(element.getStaffId())
                    .costByTime(StringUtils.convertNumberToCost((int) Math.round(element.getPriceTotal())))
                    .costSingle(StringUtils.convertNumberToCost((int) ((element.getTotalValue() * staffs.get(0)) / 3600000)) )
                    .costByTotalConsumption(StringUtils.convertNumberToCost(getCostByTotalConsumption(element.getTotalValue(), staffs)))
                    .build();
        }).collect(Collectors.toList());
        StatisticResponse statisticResponse = new StatisticResponse();
        statisticResponse.setBillStaffs(billStaffs);
        statisticResponse.setConsumptionMostRecentDays(statisticService.getConsumptionMostRecentDays(now));
        statisticResponse.setConsumptionPerHour(statisticService.getConsumptionPerHour(now, 1));
        statisticResponse.setConsumptionByRooms(statisticService.getConsumptionByRooms(now, 1));
        statisticResponse.setConsumptionByAppliances(statisticService.getConsumptionByAppliances(now, 1));
        return ResponseEntity.ok(statisticResponse);
    }
    @GetMapping("/recent_days")
    public ResponseEntity<?> getConsumptionMostRecentDays(@RequestParam("day") String day) {
        return ResponseEntity.ok(statisticService.getConsumptionMostRecentDays(day));
    }
    @GetMapping("/per_hour")
    public ResponseEntity<?> getConsumptionPerHour(@RequestParam("day") String day, @RequestParam("type") int type) {
        return ResponseEntity.ok(statisticService.getConsumptionPerHour(day, type));
    }
    @GetMapping("/by_rooms")
    public ResponseEntity<?> getConsumptionByRooms(@RequestParam("day") String day, @RequestParam("type") int type) {
        return ResponseEntity.ok(statisticService.getConsumptionByRooms(day, type));
    }
    @GetMapping("/by_appliances")
    public ResponseEntity<?> getConsumptionByAppliances(@RequestParam("day") String day, @RequestParam("type") int type) {
        return ResponseEntity.ok(statisticService.getConsumptionByAppliances(day, type));
    }
    @Data
    private static class StatisticResponse{
        List<BillStaff> billStaffs = new ArrayList<>();
        List<Integer> consumptionMostRecentDays = new ArrayList<>();
        List<Integer> consumptionPerHour = new ArrayList<>();
        Map<String, Integer> consumptionByRooms = new HashMap<>();
        Map<String, String> consumptionByAppliances = new HashMap<>();
    }
    @Data
    @Builder
    private static class BillStaff{
        private String month;
        private String year;
        private Integer staffType;
        private String consumption;
        private String costSingle;
        private String costByTime;
        private String costByTotalConsumption;
    }
    private Integer getCostByTotalConsumption(Long consumption, List<Integer> staffs) {
        double x = (double) consumption / 3600000;

        double sum = 0;
        double[] arr = {0, 50, 100, 200, 300, 400, 100000};
        int index = 1;
        while(x > arr[index]) {
            sum += (arr[index] - arr[index - 1]) * staffs.get(index);
            index++;
        }
        sum += (x - arr[index - 1]) * staffs.get(index);

        return (int) sum;
    }
}
