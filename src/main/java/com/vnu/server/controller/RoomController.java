package com.vnu.server.controller;

import com.vnu.server.common.FunctionPopular;
import com.vnu.server.entity.Room;
import com.vnu.server.jwt.JwtTokenProvider;
import com.vnu.server.model.MessageResponse;
import com.vnu.server.service.appliance.ApplianceService;
import com.vnu.server.service.room.RoomService;
import com.vnu.server.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;
    private final ApplianceService applianceService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createRoom(@RequestBody Room room) {
        roomService.save(room);
        return ResponseEntity.ok().body(new MessageResponse("Tạo phòng thành công!"));
    }
    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateRoom(@RequestBody Room room) {
        roomService.update(room);
        return ResponseEntity.ok().body(new MessageResponse("Cập nhật thông tin phòng thành công!"));
    }
    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteRoom(@RequestBody Room room) {
        roomService.delete(room.getId());
        return ResponseEntity.ok().body(new MessageResponse("Xóa phòng thành công!"));
    }
    @GetMapping
    public ResponseEntity<?> getRoom(@RequestParam(required = false, name = "id") Long id, HttpServletRequest request) {
        String jwt = FunctionPopular.getJwtFromRequest(request);
        String userId = jwtTokenProvider.getUserIdFromJWT(jwt);
        if(id == null) return ResponseEntity.ok().body(roomService.getAll());
        return ResponseEntity.ok().body(roomService.getById(id, Long.parseLong(userId)));
    }
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{roomId}/user/{userId}")
    public ResponseEntity<?> addUserToRoom(@PathVariable("roomId") Long roomId, @PathVariable("userId") Long userId) {
        userService.addUserToRoom(userId, roomId);
        return ResponseEntity.ok().body(new MessageResponse("Thêm user vào phòng thành công!"));
    }

}
