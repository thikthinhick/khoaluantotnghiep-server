package com.vnu.server.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Detail {
    private Integer value;
    private Long roomId;
}
