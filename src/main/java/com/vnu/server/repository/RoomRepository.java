package com.vnu.server.repository;

import com.vnu.server.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Boolean existsRoomById(Long roomId);
}
