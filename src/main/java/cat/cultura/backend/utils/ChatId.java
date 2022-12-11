package cat.cultura.backend.utils;

import org.hibernate.Hibernate;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ChatId implements Serializable {

    private Long eventId;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    private Long userId;

    public ChatId(){}

    public ChatId(Long event, Long u) {
        this.eventId = event;
        this.userId = u;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ChatId chatId = (ChatId) o;
        return eventId != null && Objects.equals(eventId, chatId.eventId)
                && userId != null && Objects.equals(userId, chatId.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, userId);
    }
}
