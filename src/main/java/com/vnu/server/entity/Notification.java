package com.vnu.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@ToString
@Builder
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String thumbnail;
    private String name;
    private String content;
    private String time;
    private boolean isNew;
    private Long applianceId;
    private Long roomId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"members", "roles"})
    private User user;

    public Notification() {

    }
}
