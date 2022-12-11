package cat.cultura.backend.entity;

import cat.cultura.backend.utils.ChatId;

import javax.persistence.*;

@Entity
public class ChatMessage {
    @EmbeddedId
    private ChatId chatId;

    @MapsId("eventId")
    @ManyToOne
    private Event event;

    @MapsId("userId")
    @ManyToOne
    private User user;

    public ChatId getChatId() {
        return chatId;
    }

    public void setChatId(ChatId chatId) {
        this.chatId = chatId;
    }

    public Event getEventChatRoom() {
        return event;
    }

    public void setEventChatRoom(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }

    @Lob
    @Column(name="content")
    private String content;

    @Column(name="timeSent")
    private String timeSent;


}
