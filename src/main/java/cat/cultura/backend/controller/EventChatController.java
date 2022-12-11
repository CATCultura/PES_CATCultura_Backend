package cat.cultura.backend.controller;

import cat.cultura.backend.dtos.ChatMessageDto;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.util.HtmlUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class EventChatController {

    @MessageMapping("/chat/{eventId}")
    @SendTo("/topic/messages/{eventId}")
    public ChatMessageDto send(@DestinationVariable String eventId, ChatMessageDto message) throws Exception {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new ChatMessageDto(message.getFrom(), message.getText(), time);
    }

}
