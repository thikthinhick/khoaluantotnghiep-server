package com.vnu.server.service.appliance;

import com.vnu.server.entity.Appliance;
import com.vnu.server.entity.Member;
import com.vnu.server.entity.Room;
import com.vnu.server.exception.ResourceNotFoundException;
import com.vnu.server.jwt.JwtTokenProvider;
import com.vnu.server.repository.ApplianceRepository;
import com.vnu.server.repository.MemberRepository;
import com.vnu.server.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplianceServiceImpl implements ApplianceService{
    private final ApplianceRepository applianceRepository;
    private final RoomRepository roomRepository;
    private final JwtTokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    @Override
    public void save(Appliance appliance, Long userId, Long roomId) {
        List<Member> members = memberRepository.findMemberByUserIdAndRoomId(userId, roomId);
        if(members.size() == 0) throw new ResourceNotFoundException("Không có quyền truy cập vào phòng!");
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Khong tim thay phong!"));
        appliance.setRoom(room);
        applianceRepository.save(appliance);
    }

    @Override
    public void update(Appliance appliance) {
        if(applianceRepository.existsApplianceById(appliance.getId())) throw new ResourceNotFoundException("khong tim thay thiet bi!");
        applianceRepository.save(appliance);
    }

    @Override
    public List<Appliance> getAll() {
        return null;
    }

    @Override
    public Appliance getById(Long applianceId) {
        return null;
    }

    @Override
    @Transactional
    public void removeApplianceToRoom(Long roomId, Long applianceId) {

    }


    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
