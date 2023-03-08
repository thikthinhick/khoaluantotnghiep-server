package com.vnu.server.model;

import com.vnu.server.entity.Appliance;
import lombok.Data;

import java.util.List;
//convert String request data to object
@Data
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
    private Long userId;
}
