package com.vnu.server.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vnu.server.entity.Appliance;
import com.vnu.server.entity.DbSchedule;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataResponse {
    private String consumptionInDay;
    private String consumptionInMonth;
    private String consumptionInYear;
    private String consumptionTotal;
    private String staffType;
    private String costLastMonth;
    private String costCurrentDay;
    private String costCurrentMonth;
    private String costTotal;
}
