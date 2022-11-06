package cat.cultura.backend.utils;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class RequestId implements Serializable {

    private Long requesterId;

    private Long friendId;

    public RequestId() {
    }

    public RequestId(Long requesterId, Long friendId) {
        this.requesterId = requesterId;
        this.friendId = friendId;
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
