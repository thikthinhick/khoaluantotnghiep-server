package com.vnu.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class DataConsumption {
    private String time;
    private int currentConsumption;
}
