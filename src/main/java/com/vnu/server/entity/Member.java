package com.vnu.server.entity;

import com.vnu.server.entity.ids.MemberId;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Member {
    @EmbeddedId
    private MemberId memberId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;
}
