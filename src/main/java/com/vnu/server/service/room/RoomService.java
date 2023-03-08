package com.vnu.server.service.room;

import com.vnu.server.entity.Room;
import com.vnu.server.model.RequestData;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RoomService {
    Room getById(Long roomId, Long userId);
    Room getById(Long roomId);
    List<Room> getAll();
    Room save(MultipartFile multipartFile, RequestData data);
    Room update(MultipartFile multipartFile, RequestData data);
    void delete(Long roomId);
}
