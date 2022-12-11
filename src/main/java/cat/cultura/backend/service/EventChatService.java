package cat.cultura.backend.service;

import cat.cultura.backend.repository.EventChatJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class EventChatService {

    private final EventChatJpaRepository chatRepo;

    public EventChatService(EventChatJpaRepository eventChatJpaRepository) {
        this.chatRepo = eventChatJpaRepository;
    }
}
