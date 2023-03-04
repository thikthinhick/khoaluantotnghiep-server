package com.vnu.server.socket;

import com.vnu.server.config.ServletAwareConfigurator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@CrossOrigin
@Component
@ServerEndpoint(value = "/websocket", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class Socket {
    private Session session;
    private String status;
    public static Set<Socket> sockets = new HashSet<>();

    @OnOpen
    public void onOpen(Session session) {
        log.info("Tạo kết nối!");
        this.session = session;
        sockets.add(this);
    }
    @OnMessage
    public void onMessage(Session session, Message message) throws IOException {
        switch (message.getTypeMessage()) {
            case "SUBSCRIBE": {
                this.status = "SUBSCRIBE";
                break;
            }
            default: break;
        }
    }
    @OnClose
    public void onClose(Session session) throws IOException {
        this.session.close();
        sockets.remove(this);
    }
    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
        log.error("Error!");
    }
    public void sendMessage(Message message) {
        try {
            this.session.getBasicRemote().sendObject(message);
        } catch (IOException e) {
            log.error("Caught exception while sending message to Session Id: " +
                    this.session.getId(), e.getMessage(), e);
        } catch (EncodeException e) {
            throw new RuntimeException(e);
        }
    }
}
