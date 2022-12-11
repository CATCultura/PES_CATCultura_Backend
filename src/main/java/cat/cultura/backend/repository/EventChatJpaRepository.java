package cat.cultura.backend.repository;

import cat.cultura.backend.entity.ChatMessage;
import cat.cultura.backend.entity.Event;
import cat.cultura.backend.utils.ChatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventChatJpaRepository extends JpaRepository<ChatMessage, ChatId> {
    List<ChatMessage> findByEvent(Event event);
}
