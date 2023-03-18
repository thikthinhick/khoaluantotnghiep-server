package com.vnu.server.service;

import com.vnu.server.entity.Appliance;
import com.vnu.server.entity.Consumption;
import com.vnu.server.exception.ResourceNotFoundException;
import com.vnu.server.model.DataConsumption;
import com.vnu.server.repository.ApplianceRepository;
import com.vnu.server.repository.ConsumptionRepository;
import com.vnu.server.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class ConsumptionService {
    private final ApplianceRepository applianceRepository;
    private final ConsumptionRepository consumptionRepository;
    @Transactional
    public void save(Long appliance_id, Consumption consumption){
        Appliance appliance = applianceRepository.findById(appliance_id).orElseThrow(() -> new ResourceNotFoundException("Khong tim thay appliance"));
        consumption.setAppliance(appliance);
        consumption.setTimeBands(calculatorTimeBands(consumption.getConsumptionTime()));
        try{
            consumptionRepository.save(consumption);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<DataConsumption> getLastConsumption(int distance) {
        List<Consumption> consumptions = consumptionRepository.getLatestConsumption(distance);
        Date now = new Date();
        int second = Integer.parseInt(convertDateToString(now, "ss"));
        second %= 15;
        now = increaseDate(now, -second);
        HashMap<String, Integer> map = new HashMap<>();
        consumptions.forEach(element -> {
            map.put(element.getTime(), element.getCurrentValue());
        });
        List<DataConsumption> data = new ArrayList<>();
        for(int i = 75 ;i >= 0; i--) {
            String date = convertDateToString(increaseDate(now, - i * 15), "yyyy-MM-dd HH:mm:ss");
            if(map.get(date) != null) data.add(new DataConsumption(date, map.get(date)));
            else data.add(new DataConsumption(date, 0));
        }
        return data;
    }
    private Date increaseDate(Date date, int distance) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.SECOND, distance);
        return c.getTime();
    }
    private String convertDateToString(Date date, String format) {
        SimpleDateFormat formatter1 = new SimpleDateFormat(format);
        return formatter1.format(date);
    }
    private int calculatorTimeBands(String date){
        Date x = StringUtils.convertStringDate(date);
        String nameDay = StringUtils.convertDateToString(x, "EEEE");
        String time = date.split(" ")[1];
        if(belong(time, "22:00:00", "23:59:59") || belong(time, "00:00:00", "04:00:00")) return 1;
        else if(!nameDay.equals("Sunday") && (belong(time, "09:30:00", "11:30:00") || belong(time, "17:00:00", "20:00:00"))) return 3;
        else return 2;
    }
    private boolean belong(String time, String startTime, String endTime) {
        return time.equals(startTime) || (time.compareTo(startTime) > 0 && endTime.compareTo(time) > 0);
    }

}
