package com.vnu.server.model;

import com.vnu.server.entity.Appliance;
import com.vnu.server.entity.DbSchedule;
import lombok.Data;
import lombok.ToString;

import java.util.List;
//convert String request data to object
@Data
@ToString
public class RequestData {
    private Long roomId;
    private String roomName;
    private String thumbnailRoom;
    private String descriptionRoom;
    private List<Long> userIds;
    private List<Appliance> appliances;


    private String applianceName;
    private String applianceDescription;
    private Boolean applianceType;
    private Long applianceId;
    private Long userId;

    private DbSchedule schedule;
    private Boolean scheduleStatus;
    private Long scheduleId;
    private Boolean typeSchedule;
    private Integer estimatedTime;
}
