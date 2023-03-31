package com.vnu.server.repository;

import com.vnu.server.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query(value = "select * from notification where user_id = ?1 order by time desc", nativeQuery = true)
    public List<Notification> getNotificationsByUserId(Long userId);
    public Boolean existsNotificationById(Long id);
    @Query(value = "select count(*) from notification where user_id = ?1 and is_new = true", nativeQuery = true)
    public Integer countIsNewNotificationByUserId(Long userId);
}
