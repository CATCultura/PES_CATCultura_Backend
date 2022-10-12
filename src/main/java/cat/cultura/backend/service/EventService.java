package cat.cultura.backend.service;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.repository.EventJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    @Autowired
    public EventJpaRepository repo;

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
        return "event removed";
    }

    public Event updateEvent(Event ev) {
        Event existingEvent=repo.findById(ev.getId()).orElse(null);
        existingEvent.setCodi((long) ev.getCodi());
        existingEvent.setDataFi(ev.getDataFi());
        return repo.save(existingEvent);
    }
}
