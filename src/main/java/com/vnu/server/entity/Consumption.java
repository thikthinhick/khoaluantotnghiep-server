package com.vnu.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties("appliance")
public class Consumption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "appliance_id")
    private Appliance appliance;
    private int currentValue;
    private int timeBands;

    private String consumptionTime;


    public Consumption(Long id, Appliance appliance, int currentValue, String consumptionTime) {
        this.id = id;
        this.appliance = appliance;
        this.currentValue = currentValue;
        this.consumptionTime = consumptionTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Appliance getAppliance() {
        return appliance;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    public void setAppliance(Appliance appliance) {
        this.appliance = appliance;
    }

    public String getTime() {
        return consumptionTime;
    }

    public void setTime(String time) {
        this.consumptionTime = time;
    }

    public int getTimeBands() {
        return timeBands;
    }

    public void setTimeBands(int timeBand) {
        this.timeBands = timeBand;
    }

    public String getConsumptionTime() {
        return consumptionTime;
    }

    public void setConsumptionTime(String consumptionTime) {
        this.consumptionTime = consumptionTime;
    }
}
