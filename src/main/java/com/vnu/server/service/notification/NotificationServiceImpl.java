package com.vnu.server.service.notification;

import com.vnu.server.entity.Appliance;
import com.vnu.server.entity.Notification;
import com.vnu.server.entity.Room;
import com.vnu.server.entity.User;
import com.vnu.server.exception.ResourceNotFoundException;
import com.vnu.server.repository.ApplianceRepository;
import com.vnu.server.repository.NotificationRepository;
import com.vnu.server.repository.UserRepository;
import com.vnu.server.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final ApplianceRepository applianceRepository;
    private final UserRepository userRepository;
    private Map<Long, SseEmitter> sseEmitters = new HashMap<>();

    @Override
    @Transactional
    public void createNotification(Long applianceId, Long userId, int notificationType) {

        switch (notificationType) {
            case 1: {
                Appliance appliance = applianceRepository.findById(applianceId).orElseThrow(() -> new ResourceNotFoundException("Not find appliance"));
                Room room = appliance.getRoom();
                List<Notification> notificationList = new ArrayList<>();
                List<Long> userIds = new ArrayList<>();
                User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Not found user"));
                room.getMembers().forEach(element -> {
                    if (!Objects.equals(element.getUser().getId(), userId)) {
                        Notification notification = Notification.builder()
                                .applianceId(applianceId)
                                .user(element.getUser())
                                .isNew(true)
                                .thumbnail(user.getThumbnail())
                                .name(user.getUsername())
                                .roomId(appliance.getRoom().getId())
                                .content("Bật " + appliance.getName() + " (" + room.getName() + ")")
                                .time(StringUtils.convertDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"))
                                .build();
                        userIds.add(element.getUser().getId());
                        notificationList.add(notification);
                    }
                });
                try {
                    notificationRepository.saveAll(notificationList);
                    sendMessage(userIds);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case 2: {
                Appliance appliance = applianceRepository.findById(applianceId).orElseThrow(() -> new ResourceNotFoundException("Not find appliance"));
                Room room = appliance.getRoom();
                List<Notification> notificationList = new ArrayList<>();
                List<Long> userIds = new ArrayList<>();
                room.getMembers().forEach(element -> {
                    User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Not found user"));
                    if (!Objects.equals(element.getUser().getId(), userId)) {
                        Notification notification = Notification.builder()
                                .applianceId(applianceId)
                                .user(element.getUser())
                                .isNew(true)
                                .thumbnail(user.getThumbnail())
                                .name(user.getUsername())
                                .roomId(appliance.getRoom().getId())
                                .content("Tắt " + appliance.getName() + " (" + room.getName() + ")")
                                .time(StringUtils.convertDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"))
                                .build();
                        userIds.add(element.getUser().getId());
                        notificationList.add(notification);
                    }
                });
                try {
                    notificationRepository.saveAll(notificationList);
                    sendMessage(userIds);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case 3: {
                Appliance appliance = applianceRepository.findById(applianceId).orElseThrow(() -> new ResourceNotFoundException("Not find appliance"));
                Room room = appliance.getRoom();
                List<Notification> notificationList = new ArrayList<>();
                List<Long> userIds = new ArrayList<>();
                room.getMembers().forEach(element -> {
                    Notification notification = Notification.builder()
                            .applianceId(applianceId)
                            .user(element.getUser())
                            .isNew(true)
                            .thumbnail(appliance.getThumbnail())
                            .name(appliance.getName())
                            .roomId(appliance.getRoom().getId())
                            .content(appliance.getName() + " (" + room.getName() + ") đã được bật theo lịch trình")
                            .time(StringUtils.convertDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"))
                            .build();
                    userIds.add(element.getUser().getId());
                    notificationList.add(notification);
                });
                try {
                    notificationRepository.saveAll(notificationList);
                    sendMessage(userIds);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case 4: {
                Appliance appliance = applianceRepository.findById(applianceId).orElseThrow(() -> new ResourceNotFoundException("Not find appliance"));
                Room room = appliance.getRoom();
                List<Notification> notificationList = new ArrayList<>();
                List<Long> userIds = new ArrayList<>();
                room.getMembers().forEach(element -> {

                    Notification notification = Notification.builder()
                            .applianceId(applianceId)
                            .user(element.getUser())
                            .isNew(true)
                            .thumbnail(appliance.getThumbnail())
                            .name(appliance.getName())
                            .roomId(appliance.getRoom().getId())
                            .content(appliance.getName() + " (" + room.getName() + ") đã được tắt theo lịch trình")
                            .time(StringUtils.convertDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"))
                            .build();
                    userIds.add(element.getUser().getId());
                    notificationList.add(notification);
                });
                try {
                    notificationRepository.saveAll(notificationList);
                    sendMessage(userIds);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case 5: {
                Appliance appliance = applianceRepository.findById(applianceId).orElseThrow(() -> new ResourceNotFoundException("Not find appliance"));
                Room room = appliance.getRoom();
                List<Notification> notificationList = new ArrayList<>();
                List<Long> userIds = new ArrayList<>();
                System.out.println(room.getMembers().size());
                room.getMembers().forEach(element -> {
                    Notification notification = Notification.builder()
                            .applianceId(applianceId)
                            .user(element.getUser())
                            .isNew(true)
                            .thumbnail(appliance.getThumbnail())
                            .name(appliance.getName())
                            .roomId(appliance.getRoom().getId())
                            .content(appliance.getName() + " (" + room.getName() + ") đang ở chế độ chờ")
                            .time(StringUtils.convertDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"))
                            .build();
                    userIds.add(element.getUser().getId());
                    notificationList.add(notification);
                });
                try {
                    notificationRepository.saveAll(notificationList);
                    sendMessage(userIds);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case 6: {
                Appliance appliance = applianceRepository.findById(applianceId).orElseThrow(() -> new ResourceNotFoundException("Not find appliance"));
                Room room = appliance.getRoom();
                List<Notification> notificationList = new ArrayList<>();
                List<Long> userIds = new ArrayList<>();
                room.getMembers().forEach(element -> {
                    Notification notification = Notification.builder()
                            .applianceId(applianceId)
                            .user(element.getUser())
                            .isNew(true)
                            .thumbnail(appliance.getThumbnail())
                            .name(appliance.getName())
                            .roomId(appliance.getRoom().getId())
                            .content(appliance.getName() + " (" + room.getName() + ") đã tự động tắt khi không sử dụng")
                            .time(StringUtils.convertDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"))
                            .build();
                    userIds.add(element.getUser().getId());
                    notificationList.add(notification);

                });
                try {
                    notificationRepository.saveAll(notificationList);
                    sendMessage(userIds);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case 7: {
                List<Long> userIds = new ArrayList<>();
                List<Notification> notifications = new ArrayList<>();
                User user1 = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Not found user"));
                Notification notification1 = Notification.builder()
                        .applianceId(null)
                        .user(user1)
                        .isNew(true)
                        .thumbnail("https://firebasestorage.googleapis.com/v0/b/applianceconsumption.appspot.com/o/house.png?alt=media")
                        .name("Hệ thống")
                        .roomId(null)
                        .content("Chào mừng bạn tới hệ thống theo dõi tiêu thụ điên POWER HOUSE")
                        .time(StringUtils.convertDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"))
                        .build();
                User user2 = userRepository.findById(3L).orElseThrow(() -> new ResourceNotFoundException("Not found user"));
                Notification notification2 = Notification.builder()
                        .applianceId(null)
                        .user(user2)
                        .isNew(true)
                        .thumbnail("https://firebasestorage.googleapis.com/v0/b/applianceconsumption.appspot.com/o/house.png?alt=media")
                        .name("Hệ thống")
                        .roomId(null)
                        .content(user1.getUsername() + " vừa đăng ký sử dụng hệ thống")
                        .time(StringUtils.convertDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"))
                        .build();
                userIds.add(userId);
                userIds.add(3L);
                notifications.add(notification1);
                notifications.add(notification2);
                try {
                    notificationRepository.saveAll(notifications);
                    sendMessage(userIds);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            default: {
                break;
            }
        }

    }

    @Override
    public SseEmitter createSseEmitter(Long userId) {
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        sseEmitters.put(userId, sseEmitter);
        sseEmitter.onCompletion(() -> sseEmitters.remove(sseEmitter));
        sseEmitter.onTimeout(() -> sseEmitters.remove(sseEmitter));
        sseEmitter.onError((e) -> sseEmitters.remove(sseEmitter));
        return sseEmitter;
    }

    private void sendMessage(List<Long> userIds) throws IOException {
        System.out.println(userIds);
        userIds.forEach(element -> {
            if (sseEmitters.get(element) != null) {
                try {
                    sseEmitters.get(element).send("Gửi thông báo mới");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }
}
