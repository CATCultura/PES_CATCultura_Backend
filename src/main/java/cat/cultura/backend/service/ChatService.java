package cat.cultura.backend.service;

import cat.cultura.backend.entity.ChatMessage;
import cat.cultura.backend.entity.Event;
import cat.cultura.backend.repository.EventChatJpaRepository;
import cat.cultura.backend.repository.EventJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private EventChatJpaRepository repo;

    @Autowired
    private UserJpaRepository userRepo;

    @Autowired
    private EventJpaRepository eventRepo;





    public ChatMessage saveMessage(ChatMessage message) {
        message.setTimeSent(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
        return repo.save(message);
    }

    public List<ChatMessage> findByEvent(Event e) {
        return repo.findByEvent(e);
    }
}
