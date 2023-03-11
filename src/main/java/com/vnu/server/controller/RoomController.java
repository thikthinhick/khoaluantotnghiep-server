package com.vnu.server.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import com.vnu.server.common.FunctionPopular;
import com.vnu.server.entity.Appliance;
import com.vnu.server.entity.Room;
import com.vnu.server.entity.User;
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
import java.util.Set;
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
        return ResponseEntity.ok().body(new MessageResponse<RoomResponse>("Tạo phòng thành công", RoomResponse.builder().roomId(room.getId())
                .currentWattage(100)
                .roomName(room.getName())
                .thumbnail(room.getThumbnail())
//                .users(room.getMembers().map)
                .build()));
    }

    @PutMapping
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateRoom(@RequestParam(required = false, name = "file") MultipartFile multipartFile, @RequestParam("data") String data) {
        Gson gson = new Gson();
        RequestData requestData = gson.fromJson(data, RequestData.class);
        roomService.update(multipartFile, requestData);
        return ResponseEntity.ok().body(new MessageResponse("Cập nhật thông tin phòng thành công!"));
    }

    @DeleteMapping("/{roomId}")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteRoom(@PathVariable("roomId") Long roomId) {
        roomService.delete(roomId);
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
                                .roomName(element.getName())
                                .thumbnail(element.getThumbnail())
                                .totalAppliances(element.getAppliances().size())
                                .users(element.getMembers().stream().map(item -> User.builder()
                                        .thumbnail(item.getUser().getThumbnail())
                                        .id(item.getUser().getId())
                                        .username(item.getUser().getUsername())
                                        .build()).collect(Collectors.toList()))
                                .build()
                );
            });
            return ResponseEntity.ok().body(new MessageResponse<List<RoomResponse>>("Lấy dữ liệu thành công!", roomResponses));
        }
//        String jwt = FunctionPopular.getJwtFromRequest(request);
//        String userId = jwtTokenProvider.getUserIdFromJWT(jwt);
//        return ResponseEntity.ok().body(roomService.getById(id, Long.parseLong(userId)));
        Room room = roomService.getById(id);
        RoomResponse roomResponse = RoomResponse.builder()
                .roomName(room.getName())
                .roomId(room.getId())
                .thumbnail(room.getThumbnail())
                .appliances(room.getAppliances())
                .descriptionRoom(room.getDescription())
                .totalConsumption(1000)
                .users(room.getMembers().stream().map(element -> User.builder()
                        .thumbnail(element.getUser().getThumbnail())
                        .id(element.getUser().getId())
                        .username(element.getUser().getUsername())
                        .build()).collect(Collectors.toList()))
                .currentWattage(100)
                .build();
        return ResponseEntity.ok().body(roomResponse);
    }


    //    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{roomId}/user/{userId}")
    public ResponseEntity<?> addUserToRoom(@PathVariable("roomId") Long roomId, @PathVariable("userId") Long userId) {
        userService.addUserToRoom(userId, roomId);
        return ResponseEntity.ok().body(new MessageResponse("Thêm user vào phòng thành công!"));
    }

    @PostMapping("/{roomId}/add_user_to_room")
    public ResponseEntity<?> addUserToRoom(@PathVariable("roomId") Long roomId, @RequestBody RequestData requestData) {
        userService.addListUserToRoom(requestData.getUserIds(), roomId);
        return ResponseEntity.ok().body(new MessageResponse("Cập nhật thành viên trong phòng thành công!"));
    }

    @Builder
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class RoomResponse {
        private Long roomId;
        private String thumbnail;
        private String roomName;
        private Integer totalAppliances;
        private String descriptionRoom;
        private Integer currentWattage;
        private Integer totalConsumption;
        private List<User> users;
        private Set<Appliance> appliances;
    }
}
