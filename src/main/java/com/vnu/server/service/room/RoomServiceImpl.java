package com.vnu.server.service.room;

import com.vnu.server.entity.Member;
import com.vnu.server.entity.Room;
import com.vnu.server.entity.User;
import com.vnu.server.exception.ResourceNotFoundException;
import com.vnu.server.jwt.JwtTokenProvider;
import com.vnu.server.repository.MemberRepository;
import com.vnu.server.repository.RoomRepository;
import com.vnu.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    @Override
    public Room getById(Long roomId, Long userId) {
        List<Member> members = memberRepository.findMemberByUserIdAndRoomId(userId, roomId);
        if(members.size() == 0) throw new ResourceNotFoundException("Bạn không có quyền truy cập");
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


    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
