package com.vnu.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Builder
@JsonIgnoreProperties("appliance")
public class Consumption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "appliance_id")
    private Appliance appliance;
    private int currentValue;

    private Timestamp consumptionTime;

    public Consumption() {

    }

    public Consumption(Long id, Appliance appliance, int currentValue, Timestamp consumptionTime) {
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

    public Timestamp getTime() {
        return consumptionTime;
    }

    public void setTime(Timestamp time) {
        this.consumptionTime = time;
    }
}
