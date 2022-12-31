package cat.cultura.backend.controller;

import cat.cultura.backend.dtos.ChatMessageDto;
import cat.cultura.backend.entity.ChatMessage;
import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.mappers.ChatMessageMapper;
import cat.cultura.backend.service.ChatService;
import cat.cultura.backend.service.EventService;
import cat.cultura.backend.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EventChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Autowired
    private UserService userService;

    @MessageMapping("/chat/{eventId}")
    @SendTo("/topic/messages/{eventId}")
    public ChatMessageDto send(@DestinationVariable String eventId, ChatMessageDto message) {
        Event associatedEvent = eventService.getEventById(message.getEventId());
        User sender = userService.getUserByUsername(message.getUserName());
        ChatMessage persistentMessage = chatMessageMapper.convertDtoToChatMessage(message);
        persistentMessage.setEvent(associatedEvent);
        persistentMessage.setUser(sender);
        ChatMessage result = chatService.saveMessage(persistentMessage);
        return chatMessageMapper.convertChatMessageToDto(result);
    }

    @GetMapping("/messages/{eventId}")
    public List<ChatMessageDto> getMessagesForEvent(@PathVariable Long eventId) {
        Event e = eventService.getEventById(eventId);
        List<ChatMessage>  temp = chatService.findByEvent(e);
        List<ChatMessageDto> result = new ArrayList<>();
        for (ChatMessage c : temp) {
            result.add(chatMessageMapper.convertChatMessageToDto(c));
        }
        return result;

    }

}
