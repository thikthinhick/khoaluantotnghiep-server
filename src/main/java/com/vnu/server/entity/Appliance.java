package com.vnu.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"consumptions"})
public class Appliance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String thumbnail;
    private String description;
    private Boolean category;
    private Boolean status;
    @OneToMany(mappedBy = "appliance")
    private Set<Consumption> consumptions = new HashSet<>();
    @OneToMany(mappedBy = "appliance")
    private Set<DbSchedule> dbSchedules = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonIgnoreProperties({"appliances", "members"})
    private Room room;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Set<Consumption> getConsumptions() {
        return consumptions;
    }

    public void setConsumptions(Set<Consumption> consumptions) {
        this.consumptions = consumptions;
    }

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

    public Set<DbSchedule> getDbSchedules() {
        return dbSchedules;
    }

    public void setDbSchedules(Set<DbSchedule> dbSchedules) {
        this.dbSchedules = dbSchedules;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Boolean getCategory() {
        return category;
    }

    public void setCategory(Boolean category) {
        this.category = category;
    }


    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
