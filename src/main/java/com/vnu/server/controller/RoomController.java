package com.vnu.server.controller;

import com.google.gson.Gson;
import com.vnu.server.common.FunctionPopular;
import com.vnu.server.entity.Room;
import com.vnu.server.jwt.JwtTokenProvider;
import com.vnu.server.model.MessageResponse;
import com.vnu.server.model.RequestData;
import com.vnu.server.service.FileFirebaseService;
import com.vnu.server.service.appliance.ApplianceService;
import com.vnu.server.service.room.RoomService;
import com.vnu.server.service.user.UserService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class RoomController {
    private final RoomService roomService;
    private final ApplianceService applianceService;
    private final FileFirebaseService fileFirebaseService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createRoom(@RequestParam(required = false, name = "file") MultipartFile multipartFile, @RequestParam("data") String data) {
        Gson gson = new Gson();
        RequestData requestData = gson.fromJson(data, RequestData.class);
        Room room = roomService.save(multipartFile, requestData);
        return ResponseEntity.ok().body(new MessageResponse<Room>("Tạo phòng thành công", room));
    }

    @PutMapping
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateRoom(@RequestParam(required = false, name = "file") MultipartFile multipartFile, @RequestParam("data") String data) {
        Gson gson = new Gson();
        RequestData requestData = gson.fromJson(data, RequestData.class);
        roomService.update(multipartFile, requestData);
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
        if (id == null) {
            List<Room> rooms = roomService.getAll();
            List<RoomResponse> roomResponses = new ArrayList<>();
            rooms.forEach(element -> {
                roomResponses.add(
                        RoomResponse.builder()
                                .roomId(element.getId())
                                .currentWattage(100)
                                .nameRoom(element.getName())
                                .thumbnail(element.getThumbnail())
                                .totalAppliances(element.getAppliances().size())
                                .listThumbnail(element.getMembers().stream().map(item -> item.getUser().getThumbnail()).collect(Collectors.toList()))
                                .build()
                );
            });
            return ResponseEntity.ok().body(new MessageResponse<List<RoomResponse>>("Lấy dữ liệu thành công!", roomResponses));
        }
        String jwt = FunctionPopular.getJwtFromRequest(request);
        String userId = jwtTokenProvider.getUserIdFromJWT(jwt);
        return ResponseEntity.ok().body(roomService.getById(id, Long.parseLong(userId)));
    }


    //    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{roomId}/user/{userId}")
    public ResponseEntity<?> addUserToRoom(@PathVariable("roomId") Long roomId, @PathVariable("userId") Long userId) {
        userService.addUserToRoom(userId, roomId);
        return ResponseEntity.ok().body(new MessageResponse("Thêm user vào phòng thành công!"));
    }

    @Builder
    @Data
    private static class RoomResponse {
        private Long roomId;
        private String thumbnail;
        private String nameRoom;
        private int totalAppliances;
        private int currentWattage;
        private List<String> listThumbnail;
    }


}
