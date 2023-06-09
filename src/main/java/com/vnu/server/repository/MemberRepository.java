package com.vnu.server.repository;

import com.vnu.server.entity.Member;
import com.vnu.server.entity.ids.MemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, MemberId> {
    @Query("select m from Member m where m.user.id = ?1")
    List<Member> findMemberByUserId(Long userid);
    @Query("select m from Member m where m.user.id = ?1 and m.room.id = ?2")
    List<Member> findMemberByUserIdAndRoomId(Long userId, Long roomId);
    @Modifying
    @Query(value = "delete from member m where room_id = ?1", nativeQuery = true)
    void deleteByRoomId(Long roomId);
    @Modifying
    @Query(value = "delete from member m where user_id = ?1", nativeQuery = true)
    void deleteByUserId(Long userId);
}
