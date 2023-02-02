package com.vnu.server.controller;

import com.vnu.server.entity.Room;
import com.vnu.server.model.MessageResponse;
import com.vnu.server.service.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;
    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody Room room) {
        roomService.save(room);
        return ResponseEntity.ok().body(new MessageResponse("Tạo phòng thành công!"));
    }
    @PutMapping
    public ResponseEntity<?> updateRoom(@RequestBody Room room) {
        roomService.update(room);
        return ResponseEntity.ok().body(new MessageResponse("Cập nhật thông tin phòng thành công!"));
    }
    @DeleteMapping
    public ResponseEntity<?> deleteRoom(@RequestBody Room room) {
        roomService.delete(room.getId());
        return ResponseEntity.ok().body(new MessageResponse("Xóa phòng thành công!"));
    }
    @GetMapping
    public ResponseEntity<?> getRoom(@RequestParam(required = false, name = "id") Long id) {
        if(id == null) return ResponseEntity.ok().body(roomService.getAll());
        return ResponseEntity.ok().body(roomService.getById(id));
    }
}
