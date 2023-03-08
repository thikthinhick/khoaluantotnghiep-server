package com.vnu.server.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataResponse {
    private String consumptionInDay;
    private String consumptionInMonth;
    private String consumptionInYear;
    private String consumptionTotal;
    private String costLastMonth;
    private String costCurrentMonth;
    private String costTotal;
}
