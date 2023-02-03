package com.vnu.server.repository;

import com.vnu.server.entity.Member;
import com.vnu.server.entity.ids.MemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, MemberId> {

}
