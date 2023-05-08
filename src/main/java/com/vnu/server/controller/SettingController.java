package com.vnu.server.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vnu.server.entity.Member;
import com.vnu.server.entity.Room;
import com.vnu.server.entity.User;
import com.vnu.server.exception.ResourceNotFoundException;
import com.vnu.server.repository.BillRepository;
import com.vnu.server.repository.MemberRepository;
import com.vnu.server.repository.RoomRepository;
import com.vnu.server.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/setting")
@AllArgsConstructor
@CrossOrigin
public class SettingController {
    private final UserRepository userRepository;
    private final BillRepository billRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;

    @GetMapping
    public ResponseEntity<?> getSetting() {
        String result = billRepository.spBillCurrent();
        List<User> users = userRepository.findUsersNotAdmin();
        users.forEach(element -> {
            element.setListRoomNames(element.getMembers().stream().map(item -> item.getRoom().getName()).collect(Collectors.toList()));
            element.setMembers(null);
        });

        SettingResponse settingResponse = SettingResponse.builder()
                .users(users)
                .staffType(result.split(",")[0])
                .staffTypeChange(result.split(",")[1])
                .build();
        return ResponseEntity.ok().body(settingResponse);
    }

    @GetMapping("/edit_user")
    public ResponseEntity<?> getEditUser(@RequestParam("user_id") Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));
        SettingResponse settingResponse = new SettingResponse();
        Set<Long> roomIds = new HashSet<>();
        List<RoomResponse> rooms =
                user.getMembers().stream().map(element -> {
                            roomIds.add(element.getRoom().getId());
                            return RoomResponse.builder().id(element.getRoom().getId())
                                    .checked(true)
                                    .roomName(element.getRoom().getName())
                                    .thumbnail(element.getRoom().getThumbnail())
                                    .build();
                        }
                ).collect(Collectors.toList());
        roomRepository.findRoomByIdNotIn(roomIds).forEach(
                element -> rooms.add(RoomResponse.builder().id(element.getId())
                        .checked(false)
                        .roomName(element.getName())
                        .thumbnail(element.getThumbnail())
                        .build()));
        if(roomIds.size() == 0) {
            roomRepository.findAll().forEach(
                    element -> rooms.add(RoomResponse.builder().id(element.getId())
                            .checked(false)
                            .roomName(element.getName())
                            .thumbnail(element.getThumbnail())
                            .build()));
        }
        settingResponse.setUserName(user.getUsername());
        settingResponse.setRooms(rooms);
        settingResponse.setActive(user.getActive());
        return ResponseEntity.ok(settingResponse);
    }
    @PutMapping("/edit_user")
    @Transactional
    public ResponseEntity<?> updateUser(@RequestBody SettingRequest settingRequest) {
        User user = userRepository.findById(settingRequest.getId()).orElseThrow(() -> new ResourceNotFoundException("Khong tim thay nguoi dung!"));
        user.setActive(settingRequest.getActive());
        List<Room> rooms = roomRepository.findRoomByIdIn(settingRequest.getRoomIds());
        List<Member> members = new ArrayList<>();
        rooms.forEach(element -> {
            Member member = new Member();
            member.setUser(user);
            member.setRoom(element);
            members.add(member);
        });
        memberRepository.deleteByUserId(settingRequest.getId());
        memberRepository.saveAll(members);
        return ResponseEntity.ok("Cap nhat thanh cong!");
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NoArgsConstructor
    @AllArgsConstructor
    private static class SettingResponse {
        private List<User> users;
        private String staffType;
        private String staffTypeChange;
        private String userName;
        private Boolean active;
        private List<RoomResponse> rooms;
    }

    @Data
    @Builder
    private static class RoomResponse {
        private Long id;
        private String roomName;
        private String thumbnail;
        private Boolean checked;
    }
    @Data
    private static class SettingRequest {
        private Long id;
        private Boolean active;
        private List<Long> roomIds;
    }
}
