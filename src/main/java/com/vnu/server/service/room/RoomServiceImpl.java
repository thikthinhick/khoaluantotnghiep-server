package com.vnu.server.service.room;

import com.vnu.server.entity.Room;
import com.vnu.server.exception.ResourceNotFoundException;
import com.vnu.server.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    @Override
    public Room getById(Long roomId) {
        return roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Phòng " + roomId + " không được tìm thấy!"));
    }

    @Override
    public List<Room> getAll() {
        return roomRepository.findAll();
    }

    @Override
    public Room save(Room room) {
        roomRepository.save(room);
        return room;
    }

    @Override
    public void update(Room room) {
        roomRepository.findById(room.getId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng!"));
        roomRepository.save(room);
    }

    @Override
    public void delete(Long roomId) {
        roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Khong tim thay phong!"));
        roomRepository.deleteById(roomId);
    }
}
