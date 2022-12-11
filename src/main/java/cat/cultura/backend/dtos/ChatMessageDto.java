package cat.cultura.backend.dtos;

public class ChatMessageDto {

    private String from;

    private String text;

    private String date;

    public ChatMessageDto(String from, String text, String time) {
        this.from = from;
        this.text = text;
        this.date = time;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
