package com.vnu.server.service.room;

import com.vnu.server.entity.Room;

import java.util.List;

public interface RoomService {
    Room getById(Long roomId);
    List<Room> getAll();
    Room save(Room room);
    void update(Room room);
    void delete(Long roomId);
}
