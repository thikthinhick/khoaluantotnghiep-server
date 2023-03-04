package com.vnu.server;

import com.vnu.server.service.ConsumptionService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.SimpleDateFormat;
import java.util.Date;


@SpringBootApplication
@AllArgsConstructor
public class ServerApplication implements CommandLineRunner {
 private final ConsumptionService consumptionService;

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws InterruptedException {
//        Role role = new Role();
//        role.setName("USER");
//        roleRepository.save(role);
//        User user = new User();
//        user.setEmail("trumle2k1@gmail.com");
//        user.setUsername("chuong2001");
//        user.setPassword(passwordEncoder.encode("chuong2001"));
//        user.setRoles(new HashSet<>(Arrays.asList(role)));
//        userRepository.save(user);
//        Room room = new Room();
//        room.setDescription("Nơi sinh hoạt của gia đình");
//        room.setThumbnail("https://google.com.vn");
//        room.setName("Nhà bếp");
//        roomRepository.save(room);
//        Appliance appliance = new Appliance();
//        appliance.setCategory(true);
//        appliance.setThumbnail("https://google.com.vn");
//        appliance.setName("Tủ lạnh");
//        appliance.setRoom(room);
//        applianceRepository.save(appliance);

    }

    private String convertDateToString(Date date, String format) {
        SimpleDateFormat formatter1 = new SimpleDateFormat(format);
        return formatter1.format(date);
    }


}
