package cat.cultura.backend.service;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.repository.EventJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventJpaRepository repo;

    public EventService(EventJpaRepository repo) {
        this.repo = repo;
    }

    public Event saveEvent(Event ev) {
        return repo.save(ev);
    }

    public List<Event> saveEvents(List<Event> evs) {
        return repo.saveAll(evs);
    }

    public List<Event> getEvents() {
        return repo.findAll();
    }

    public Event getEventByID(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Event getEventByCodi(Long codi) {
        return repo.findByCodi(codi);
    }

    public String deleteEvent(Long id) {
        repo.deleteById(id);
        return "Event with id: " + id + " removed";
    }

    public Event updateEvent(Event ev) {
        return repo.save(ev);
    }
}