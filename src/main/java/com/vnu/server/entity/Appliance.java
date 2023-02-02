package com.vnu.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Appliance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String status;
    private String thumbnail;
    private Boolean category;
    private Boolean running;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "room_id")
    private Room room;
}
