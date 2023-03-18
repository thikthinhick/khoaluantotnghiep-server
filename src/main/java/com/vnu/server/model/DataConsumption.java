package com.vnu.server.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataConsumption {
    private String time;
    private int currentConsumption;
    private Long roomId;
    private Long applianceId;
    public DataConsumption(String time, int currentConsumption) {
        this.time = time;
        this.currentConsumption = currentConsumption;
    }
}
