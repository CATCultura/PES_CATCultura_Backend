package cat.cultura.backend.mappers;

import cat.cultura.backend.dtos.ChatMessageDto;
import cat.cultura.backend.entity.ChatMessage;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageMapper {

    private final ModelMapper modelMapper;
    public ChatMessageMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ChatMessageDto convertChatMessageToDto(ChatMessage chatMessage) {
        ChatMessageDto result = modelMapper.map(chatMessage,ChatMessageDto.class);
        result.setEventId(chatMessage.getEvent().getId());
        result.setUserId(chatMessage.getUser().getId().toString());
        result.setUserName(chatMessage.getUser().getUsername());
        return result;
    }

    public ChatMessage convertDtoToChatMessage(ChatMessageDto message) {
        return modelMapper.map(message,ChatMessage.class);

    }

}
