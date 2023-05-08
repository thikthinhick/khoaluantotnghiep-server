package com.vnu.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vnu.server.entity.ids.MemberId;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@JsonIgnoreProperties({"room", "memberId"})
public class Member {
    @EmbeddedId
    private MemberId memberId = new MemberId();
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("members")
    private User user;
    @ManyToOne
    @MapsId("roomId")
    @JoinColumn(name = "room_id")
    private Room room;

    public Member() {

    }
    public MemberId getMemberId() {
        return memberId;
    }

    public void setMemberId(MemberId memberId) {
        this.memberId = memberId;
    }



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
