package com.vnu.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Appliance {
    private String name;
    private String thumbnail;
    private double value;
}
