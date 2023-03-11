package com.vnu.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.ToString;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import java.sql.Time;

@Entity
@ToString
@JsonIgnoreProperties("appliance")
public class DbSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Time startDate;
    private Time endDate;
    private String repeatDay;
    @Column(columnDefinition = "boolean default false")
    private Boolean status;
    @ManyToOne
    @JoinColumn(name = "appliance_id")
    private Appliance appliance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Time getStartDate() {
        return startDate;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setStartDate(Time startDate) {
        this.startDate = startDate;
    }

    public Time getEndDate() {
        return endDate;
    }

    public void setEndDate(Time endDate) {
        this.endDate = endDate;
    }

    public Appliance getAppliance() {
        return appliance;
    }

    public String getRepeatDay() {
        return repeatDay;
    }

    public void setRepeatDay(String repeatDay) {
        this.repeatDay = repeatDay;
    }

    public void setAppliance(Appliance appliance) {
        this.appliance = appliance;
    }
}
