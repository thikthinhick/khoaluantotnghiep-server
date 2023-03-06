package com.vnu.server;

import com.google.gson.Gson;
import com.vnu.server.service.ConsumptionService;
import com.vnu.server.service.FileFirebaseService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
@AllArgsConstructor
public class ServerApplication implements CommandLineRunner {
    private final ConsumptionService consumptionService;
    private final FileFirebaseService fileFirebaseService;

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws InterruptedException {
        String json = "{userIds:[1,1,1,1,1,1]}";
        Gson gson = new Gson();
        Object object = gson.fromJson(json, Test.class);
        System.out.println(object);

    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Test{
        List<Integer> userIds = new ArrayList<>();
    }


}
