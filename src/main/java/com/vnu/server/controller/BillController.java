package com.vnu.server.controller;

import com.vnu.server.model.CSModel;
import com.vnu.server.repository.BillRepository;
import com.vnu.server.repository.ConsumptionRepository;
import com.vnu.server.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/bill")
@AllArgsConstructor
@CrossOrigin
public class BillController {
    private final BillRepository billRepository;
    private final ConsumptionRepository consumptionRepository;
    @PutMapping("/{id}")
    public void updateBill(@PathVariable("id") Integer id) {
        billRepository.spBillUpdate(id);
    }
    @DeleteMapping
    public void deleteBillSchedule() {
        String nextMonth = StringUtils.nextOneMonth(new Date()) + "-01";
        billRepository.deleteBillByTime(nextMonth);
    }
    @GetMapping
    public List<CSModel> getCmsModel() {
        return consumptionRepository.getBillAll();
    }
    @Data
    private static class BillRequest{
        private String time;
    }
}
