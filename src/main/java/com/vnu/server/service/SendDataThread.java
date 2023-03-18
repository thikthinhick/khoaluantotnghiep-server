package com.vnu.server.service;

import com.vnu.server.model.DataConsumption;
import com.vnu.server.socket.MessageSocket;
import com.vnu.server.socket.Socket;
import com.vnu.server.utils.StringUtils;

import java.util.Date;

public class SendDataThread extends Thread {
    public static int count;
    private int limit;

    public SendDataThread(int limit) {
        this.limit = limit;
    }

    @Override
    public void run() {
        while (true) {
            count++;
            try {
                if (count == limit) {

                    Socket.sockets.forEach(element -> {
                        element.sendMessage(MessageSocket.builder().typeMessage("CHART_HOME")
                                .data (new DataConsumption(StringUtils.convertDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"), 0))
                                .build());
                    });
                    count = 0;
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
