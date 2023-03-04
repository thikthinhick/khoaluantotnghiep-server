package com.vnu.server.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataResponse {
    private String consumptionInDay;
    private String consumptionInMonth;
    private String consumptionInYear;
    private String consumptionTotal;
    private String costLastMonth;
    private String costCurrentMonth;
    private String costTotal;
}
