package com.vnu.server.socket;


import com.google.gson.Gson;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<MessageSocket> {

    private static Gson gson = new Gson();

    @Override
    public MessageSocket decode(String s) throws DecodeException {
        MessageSocket message = gson.fromJson(s, MessageSocket.class);
        return message;
    }

    @Override
    public boolean willDecode(String s) {
        return (s != null);
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