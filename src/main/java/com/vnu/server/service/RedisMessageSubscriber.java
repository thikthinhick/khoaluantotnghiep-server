package com.vnu.server.service;

import com.google.gson.Gson;
import com.vnu.server.entity.Consumption;
import com.vnu.server.model.DataConsumption;
import com.vnu.server.model.Detail;
import com.vnu.server.model.MessageConsumption;
import com.vnu.server.service.notification.NotificationService;
import com.vnu.server.socket.MessageSocket;
import com.vnu.server.socket.Socket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

@Service
@Slf4j
public class RedisMessageSubscriber implements MessageListener {
    private HashMap<Long, Integer> data = new HashMap<>();
    private List<Long> sessionStandby = new ArrayList<>();
    private final ConsumptionService consumptionService;
    private final NotificationService notificationService;

    public RedisMessageSubscriber(ConsumptionService consumptionService, NotificationService notificationService) {
        this.consumptionService = consumptionService;
        this.notificationService = notificationService;
    }

    private SendDataThread thread = new SendDataThread(20);

    @PostConstruct
    private void init() {
        thread.start();
    }

    private int total = 0;

    public void onMessage(Message message, byte[] pattern) {
        try {
            Object object = deserialize(message.getBody());
            Gson gson = new Gson();
            MessageConsumption messageConsumption = gson.fromJson((String) object, MessageConsumption.class);
            Set<Long> keys = messageConsumption.getData().keySet();
            HashMap<Long, Integer> roomsData = new HashMap<>();
            for (Long key : keys) {
                int value = messageConsumption.getData().get(key).getValue();
                Long roomId = messageConsumption.getData().get(key).getRoomId();
                data.merge(key, value, Integer::sum);
                roomsData.merge(roomId, value, Integer::sum);
                total += value;
            }
            int second = Integer.parseInt(messageConsumption.getTime().split(":")[2]);
            if (second % 15 == 0) {
                int sum = 0;
                for (Long key : data.keySet()) {
                    consumptionService.save(key,
                            Consumption.builder()
                                    .consumptionTime(messageConsumption.getTime())
                                    .currentValue(data.get(key))
                                    .build());
                    sum += data.get(key);
                }
                int finalSum = sum;
                Socket.sockets.forEach(element -> {
                    if (element.getStatus() != null && element.getStatus().equals("SUBSCRIBE_HOME")) {
                        element.sendMessage(MessageSocket.builder()
                                .typeMessage("CHART_HOME")
                                .data(new DataConsumption(messageConsumption.getTime(), finalSum / 15))
                                .build());
                    }
                });
                SendDataThread.count = 0;
                data = new HashMap<>();
            }
            Socket.sockets.forEach(element -> {
                if (element.getStatus() != null && element.getStatus().equals("SUBSCRIBE_ROOMS")) {
                    element.sendMessage(MessageSocket.builder()
                            .typeMessage("SPEED_METTER_ROOMS")
                            .data(roomsData)
                            .build());
                }
                if (element.getStatus() != null && element.getStatus().equals("SUBSCRIBE_HOME")) {
                    element.sendMessage(MessageSocket.builder().typeMessage("SPEED_METTER")
                            .data(new DataConsumption(messageConsumption.getTime(), total))
                            .build());
                }
                if (element.getStatus() != null && element.getStatus().equals("SUBSCRIBE_ROOM")) {
                    HashMap<Long, Wattage> wattInRoom = new HashMap<>();
                    for (Long key : keys) {
                        Detail detail = messageConsumption.getData().get(key);
                        if (Objects.equals(detail.getRoomId(), element.getRoomId()))
                            wattInRoom.put(key, Wattage.builder()
                                    .value(detail.getValue())
                                    .standBy(detail.getStandBy())
                                    .build());
                    }
                    element.sendMessage(MessageSocket.builder().typeMessage("SPEED_METTER_ROOM")
                            .data(wattInRoom)
                            .build());
                }
                if (element.getStatus() != null && element.getStatus().equals("SUBSCRIBE_APPLIANCE")) {
                    Detail detail = messageConsumption.getData().get(element.getApplianceId());
                    element.sendMessage(MessageSocket.builder().typeMessage("SPEED_METER")
                            .data(detail)
                            .build());
                }
            });
            for (Long key : keys) {
                Boolean standby = messageConsumption.getData().get(key).getStandBy();
                if(standby && !sessionStandby.contains(key)) {
                    notificationService.createNotification(key, null, 5);
                    sessionStandby.add(key);
                }
                else if(!standby) {
                    sessionStandby.remove(key);
                }
            }
            sessionStandby.removeIf(id -> !keys.contains(id));
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        total = 0;
    }

    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    @Builder
    private static class Wattage {
        private Integer value;
        private Boolean standBy;
    }
}