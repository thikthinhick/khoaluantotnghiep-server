package com.vnu.server.model;

import org.springframework.beans.factory.annotation.Value;


public interface CSModel {
     @Value(value = "#{target.time}")
     String getTime();
     @Value(value = "#{target.name}")
     String getName();
     @Value(value = "#{target.value}")
     Integer getValue();
     @Value(value = "#{target.staff_id}")
     Integer getStaffId();
     @Value(value = "#{target.total_value}")
     Long getTotalValue();
     @Value(value = "#{target.total_price}")
     Double getPriceTotal();
}
