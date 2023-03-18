package com.vnu.server.socket;


import com.google.gson.Gson;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<MessageSocket> {

    private static Gson gson = new Gson();

    @Override
    public String encode(MessageSocket message) throws EncodeException {
        String json = gson.toJson(message);
        return json;
    }
    @Override
    public void init(EndpointConfig endpointConfig) {
        // Custom initialization logic
    }

    @Override
    public void destroy() {
        // Close resources
    }
}