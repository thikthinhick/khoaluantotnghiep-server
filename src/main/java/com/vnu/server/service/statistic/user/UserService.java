package com.vnu.server.service.statistic.user;

import com.vnu.server.entity.User;

import java.util.List;

public interface UserService {
    void addUserToRoom(Long userId, Long roomId);
    void addListUserToRoom(List<Long > ids, Long roomId);
    void updateUser(User user);
}
