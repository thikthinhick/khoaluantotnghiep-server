package com.vnu.server.repository;

import com.vnu.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);
    User findUserByEmail(String email);
    @Query(value = "select * from user a inner join users_roles b on a.id = b.user_id where b.role_id = 1", nativeQuery = true)
    List<User> findUsersNotAdmin();
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
