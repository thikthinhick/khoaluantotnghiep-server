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
    public void createNotification(Long applianceId, Long userId, int notificationType) {
        Appliance appliance = applianceRepository.findById(applianceId).orElseThrow(() -> new ResourceNotFoundException("Not find appliance"));

        Room room = appliance.getRoom();
        switch (notificationType) {
            case 1: {
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
        userIds.forEach(element -> {
            if(sseEmitters.get(element) != null) {
                try {
                    sseEmitters.get(element).send("");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }
}
