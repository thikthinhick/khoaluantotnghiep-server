package com.vnu.server.controller;

import com.vnu.server.entity.Notification;
import com.vnu.server.model.MessageResponse;
import com.vnu.server.repository.NotificationRepository;
import com.vnu.server.service.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notification")
@CrossOrigin
@Slf4j
@AllArgsConstructor
public class NotificationController {
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<?> getAllNotificationByUserId(@RequestParam("user_id") Long userId) {
        List<Notification> notificationList = notificationRepository.getNotificationsByUserId(userId);
        Map<Long, Boolean> map = new HashMap<>();
        List<Notification> notificationsUpdate = notificationList.stream().peek(element -> {
            if (element.isNew()) {
                element.setNew(false);
                map.put(element.getId(), true);
            }
        }).collect(Collectors.toList());
        notificationRepository.saveAll(notificationsUpdate);
        notificationList.forEach(element -> {
            if (map.get(element.getId()) != null) element.setNew(true);
        });
        return ResponseEntity.ok().body(new MessageResponse<>("Lấy thông tin thành công", notificationList));
    }

    @GetMapping("/get_total")
    public ResponseEntity<?> getTotalNewNotificationByUserId(@RequestParam("user_id") Long userId) {
        return ResponseEntity.ok().body(new MessageResponse<>("Lấy thông tin thành công", notificationRepository.countIsNewNotificationByUserId(userId)));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteNotification(@RequestParam("id") Long id) {
        if (!notificationRepository.existsNotificationById(id))
            return ResponseEntity.badRequest().body(new MessageResponse<>("Thông báo xóa không tồn tại!"));
        notificationRepository.deleteById(id);
        return ResponseEntity.ok().body(new MessageResponse<>("Xóa thông báo thành công!"));
    }

    @GetMapping("/receive")
    public SseEmitter streamDateTime(@RequestParam("user_id") Long userId) {
        log.info(userId + " tạo kết nối!");
        SseEmitter sseEmitter = notificationService.createSseEmitter(userId);
        return sseEmitter;
    }
}
