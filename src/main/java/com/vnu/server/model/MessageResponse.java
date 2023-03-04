package com.vnu.server.model;

import lombok.Data;

@Data
public class MessageResponse<T> {
    private String message;
    private T info;
    public MessageResponse(String message) {
        this.message = message;
    }
    public MessageResponse(String message, T info) {
        this.info = info;
    }
}
