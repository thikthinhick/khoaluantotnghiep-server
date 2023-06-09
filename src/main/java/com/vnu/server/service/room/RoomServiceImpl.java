package com.vnu.server.service.room;

import com.vnu.server.entity.Member;
import com.vnu.server.entity.Room;
import com.vnu.server.entity.User;
import com.vnu.server.exception.ResourceNotFoundException;
import com.vnu.server.model.RequestData;
import com.vnu.server.repository.MemberRepository;
import com.vnu.server.repository.RoomRepository;
import com.vnu.server.repository.UserRepository;
import com.vnu.server.service.FileFirebaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final FileFirebaseService fileFirebaseService;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    @Override
    public Room getById(Long roomId, Long userId) {
        List<Member> members = memberRepository.findMemberByUserIdAndRoomId(userId, roomId);
        if(members.size() == 0) throw new ResourceNotFoundException("Bạn không có quyền truy cập");
        return roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Phòng " + roomId + " không được tìm thấy!"));
    }

    @Override
    public Room getById(Long roomId) {
        return roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Phòng " + roomId + " không được tìm thấy!"));
    }

    @Override
    public List<Room> getAll() {
        return roomRepository.findAll();
    }

    @Override
    @Transactional
    public Room save(MultipartFile multipartFile, RequestData data) {
        Room room = new Room();
        saveRoom(multipartFile, data, room);
        User user = userRepository.findById(3L).orElseThrow(() -> new ResourceNotFoundException("Not found user!"));
        Member member = new Member();
        member.setRoom(room);
        member.setUser(user);
        memberRepository.save(member);
        return room;
    }

    @Override
    public Room update(MultipartFile multipartFile, RequestData data) {
        Room room = roomRepository.findById(data.getRoomId()).orElseThrow(() -> new ResourceNotFoundException("Khong tim thay phong"));
        saveRoom(multipartFile, data, room);
        return room;
    }

    private void saveRoom(MultipartFile multipartFile, RequestData data, Room room) {
        if(data.getThumbnailRoom() != null)
        room.setThumbnail(data.getThumbnailRoom());
        room.setDescription(data.getDescriptionRoom());
        room.setName(data.getRoomName());
        if(multipartFile != null) {
            String urlImage = fileFirebaseService.upload(multipartFile);
            room.setThumbnail(urlImage);
        }
        roomRepository.save(room);
    }

    @Override
    @Transactional
    public void delete(Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Khong tim thay phong!"));
        memberRepository.deleteAll(room.getMembers());
        roomRepository.deleteById(roomId);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
