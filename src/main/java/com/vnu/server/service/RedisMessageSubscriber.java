package com.vnu.server.service;

import com.google.gson.Gson;
import com.vnu.server.entity.Consumption;
import com.vnu.server.model.DataConsumption;
import com.vnu.server.model.MessageConsumption;
import com.vnu.server.socket.Socket;
import com.vnu.server.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Set;

@Service
@Slf4j
public class RedisMessageSubscriber implements MessageListener {
    private HashMap<Long, Integer> data = new HashMap<>();
    private final ConsumptionService consumptionService;

    public RedisMessageSubscriber(ConsumptionService consumptionService) {
        this.consumptionService = consumptionService;
    }

    private SendDataThread thread = new SendDataThread( 20);

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
            for (Long key : keys) {
                int value = messageConsumption.getData().get(key);
                data.merge(key, value, Integer::sum);
                total += value;
            }
            int second = Integer.parseInt(messageConsumption.getTime().split(":")[2]);
            if (second % 15 == 0) {
                int sum = 0;
                for (Long key : data.keySet()) {
                    consumptionService.save(key,
                            Consumption.builder()
                                    .consumptionTime(StringUtils.convertStringToTimestamp(messageConsumption.getTime()))
                                    .currentValue(data.get(key))
                                    .build());
                    sum += data.get(key);
                }
                int finalSum = sum;
                Socket.sockets.forEach(element -> {
                    element.sendMessage(new com.vnu.server.socket.Message("CHART_HOME", new DataConsumption(messageConsumption.getTime(), finalSum)));
                });
                SendDataThread.count = 0;
                data = new HashMap<>();
            }
            Socket.sockets.forEach(element -> {
                element.sendMessage(new com.vnu.server.socket.Message("SPEED_METTER", new DataConsumption(messageConsumption.getTime(), total)));
            });
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
}