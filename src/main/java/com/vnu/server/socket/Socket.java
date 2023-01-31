package com.vnu.server.socket;

import com.vnu.server.config.ServletAwareConfigurator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@Slf4j
@CrossOrigin
@Component
@ServerEndpoint(value="/websocket", encoders = MessageEncoder.class, decoders = MessageDecoder.class, configurator = ServletAwareConfigurator.class)
public class Socket {
    private Session session;
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {

    }
    @OnMessage
    public void onMessage(Session session, Message message) throws IOException {

    }
    @OnClose
    public void onClose(Session session) {

    }
    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
        log.error("Error!");
    }
}
