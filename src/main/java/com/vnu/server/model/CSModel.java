package com.vnu.server.model;

import org.springframework.beans.factory.annotation.Value;


public interface CSModel {
     @Value(value = "#{target.time}")
     String getTime();
     @Value(value = "#{target.value}")
     Integer getValue();
}
