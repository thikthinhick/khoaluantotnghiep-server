package com.vnu.server.repository;

import com.vnu.server.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Boolean existsRoomById(Long roomId);
    @Query(value = "select * from room where room.id not in ?1", nativeQuery = true)
    List<Room> findRoomByIdNotIn(Set<Long> ids);
    @Query(value = "select * from room where room.id in ?1", nativeQuery = true)
    List<Room> findRoomByIdIn(List<Long> ids);
}
