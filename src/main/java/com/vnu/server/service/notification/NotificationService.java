package com.vnu.server.service.notification;

import com.vnu.server.entity.Notification;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
    void createNotification(Long applianceId, Long userId, int notificationType);
    SseEmitter createSseEmitter(Long userId);
}
