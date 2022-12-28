package cat.cultura.backend.utils;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RequestId implements Serializable {

    private Long requesterId;

    private Long friendId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestId requestId = (RequestId) o;
        return requesterId.equals(requestId.requesterId) && friendId.equals(requestId.friendId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requesterId, friendId);
    }

    public RequestId() {
    }

    public RequestId(Long requesterId, Long friendId1) {
        this.requesterId = requesterId;
        this.friendId = friendId1;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }
}
