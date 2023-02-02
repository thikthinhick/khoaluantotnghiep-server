package com.vnu.server.entity.ids;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
public class MemberId implements Serializable {
    private static final long serialVersionUID = 1L;
   private Long userId;
   private Long roomId;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemberId)) return false;
        MemberId memberId = (MemberId) o;
        return Objects.equals(getUserId(), memberId.getUserId()) &&
                Objects.equals(getRoomId(), memberId.getRoomId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getUserId(), getRoomId());
    }
}
