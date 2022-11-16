package cat.cultura.backend.entity;

import cat.cultura.backend.utils.RequestId;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "friend_request")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY)
public class Request {

    @EmbeddedId
    private RequestId requestId;

    @MapsId("requesterId")
    @ManyToOne
    private User requester;

    @MapsId("friendId")
    @ManyToOne
    private User friend;

    Request() {}

    public Request(User requester, User friend) {
        this.requestId = new RequestId(requester.getId(),friend.getId());
        this.requester = requester;
        this.friend = friend;

    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public RequestId getRequestId() {
        return requestId;
    }

    public void setRequestId(RequestId requestId) {
        this.requestId = requestId;
    }

}
