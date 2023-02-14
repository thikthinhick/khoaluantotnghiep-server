package com.vnu.server.service.user;

import com.vnu.server.entity.User;

public interface UserService {
    void addUserToRoom(Long userId, Long roomId);
    void updateUser(User user);
}
