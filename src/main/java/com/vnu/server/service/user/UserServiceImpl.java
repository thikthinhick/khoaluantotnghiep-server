package com.vnu.server.service.user;

import com.vnu.server.entity.Member;
import com.vnu.server.entity.Role;
import com.vnu.server.entity.Room;
import com.vnu.server.entity.User;
import com.vnu.server.exception.ResourceNotFoundException;
import com.vnu.server.jwt.JwtTokenProvider;
import com.vnu.server.model.MyUserDetails;
import com.vnu.server.repository.MemberRepository;
import com.vnu.server.repository.RoleRepository;
import com.vnu.server.repository.RoomRepository;
import com.vnu.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.vnu.server.entity.Role.RoleName.READ;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider tokenProvider;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new MyUserDetails(user);
    }

    @Override
    public void addUserToRoom(Long userId, Long roomId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Not found user!"));
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Not found room!"));
//        Role role = roleRepository.findByName(READ.name()).orElseThrow(() -> new ResourceNotFoundException("Not found role!"));
        Member member = new Member();
        member.setUser(user);
        member.setRoom(room);
//        member.setRole(role);
        try{
            memberRepository.save(member);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void addListUserToRoom(List<Long> ids, Long roomId) {
        memberRepository.deleteByUserId(roomId);
        ids.forEach(userId -> {
           this.addUserToRoom(userId, roomId);
        });
    }

    @Override
    public void updateUser(User user) {
        if(userRepository.existsByUsername(user.getUsername())) throw new ResourceNotFoundException("Not found user!");
        User u = userRepository.findUserByUsername(user.getUsername());
    }
}

