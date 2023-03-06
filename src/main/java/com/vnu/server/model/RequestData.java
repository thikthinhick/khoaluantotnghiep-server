package com.vnu.server.model;

import lombok.Data;

import java.util.List;

@Data
public class RequestData {
    private Long roomId;
    private String roomName;
    private String thumbnailRoom;
    private String descriptionRoom;
    private List<Long> userIds;
}
