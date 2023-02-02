package com.vnu.server.controller;

import com.vnu.server.model.Appliance;
import com.vnu.server.socket.Message;
import com.vnu.server.socket.Socket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/appliance")
@Slf4j
public class ApplianceController {
    @PostMapping
    public void sendData(@RequestBody MessageAppliance formRequest) {
        Socket.sockets.forEach(
                socket -> socket.sendMessage(new Message("JOIN", "hello "))
        );
    }
    @Data
    @AllArgsConstructor
    private static class MessageAppliance{
        private String message;
        private List<Appliance> appliances;
    }
}
