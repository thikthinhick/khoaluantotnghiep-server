package com.vnu.server.socket;

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
    private String status = "NONE";
    private Long roomId;
    private Long applianceId;
    public static Set<Socket> sockets = new HashSet<>();

    @OnOpen
    public void onOpen(Session session) {
        log.info("Tạo kết nối!");
        this.session = session;
        sockets.add(this);
    }
    @OnMessage
    public void onMessage(Session session, MessageSocket message) throws IOException {
        this.status = "NONE";
        this.roomId = null;
        this.applianceId = null;
        switch (message.getTypeMessage()) {
            case "SUBSCRIBE_HOME": {
                this.status = "SUBSCRIBE_HOME";
                break;
            }
            case "SUBSCRIBE_ROOMS": {
                this.status = "SUBSCRIBE_ROOMS";
                break;
            }
            case "SUBSCRIBE_ROOM": {
                this.status = "SUBSCRIBE_ROOM";
                this.roomId = message.getRoomId();
                break;
            }
            case "SUBSCRIBE_APPLIANCE": {
                this.status = "SUBSCRIBE_ROOM";
                this.applianceId = message.getApplianceId();
                break;
            }
            default: {
                this.status = null;
                break;
            }
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
    public void sendMessage(MessageSocket message) {
        try {
            this.session.getBasicRemote().sendObject(message);
        } catch (IOException e) {
            log.error("Caught exception while sending message to Session Id: " +
                    this.session.getId(), e.getMessage(), e);
        } catch (EncodeException e) {
            throw new RuntimeException(e);
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getApplianceId() {
        return applianceId;
    }

    public void setApplianceId(Long applianceId) {
        this.applianceId = applianceId;
    }
}
