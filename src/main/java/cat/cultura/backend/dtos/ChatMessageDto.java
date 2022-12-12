package cat.cultura.backend.dtos;

public class ChatMessageDto {

    private Long eventId;

    private Long id;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;

    private String userName;

    private String content;

    private String timeSent;

    public ChatMessageDto(String userName, String content, String time) {
        this.userName = userName;
        this.content = content;
        this.timeSent = time;
    }

    public ChatMessageDto() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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


}
