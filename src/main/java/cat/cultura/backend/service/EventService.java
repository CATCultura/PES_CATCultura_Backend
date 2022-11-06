package cat.cultura.backend.service;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.exceptions.EventNotFoundException;
import cat.cultura.backend.exceptions.TrophyNotFoundException;
import cat.cultura.backend.repository.EventJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventJpaRepository eventRepo;

    public EventService(EventJpaRepository eventRepo) {
        this.eventRepo = eventRepo;
    }

    public Event saveEvent(Event ev) {
        return eventRepo.save(ev);
    }

    public List<Event> saveEvents(List<Event> evs) {
        return eventRepo.saveAll(evs);
    }

    public List<Event> getEvents() {
        List<Event> result = eventRepo.findAll();
        if(result.isEmpty()) throw new TrophyNotFoundException("No Events found\n");
        else return result;
    }

    public Event getEventByID(Long id) {
        return eventRepo.findById(id).orElseThrow(()-> new EventNotFoundException("Event with id: " + id + " not found\n"));
    }

    public Event getEventByCodi(Long codi) {
        return eventRepo.findByCodi(codi).orElseThrow(()-> new EventNotFoundException("Event with code: " + codi + " not found\n"));
    }

    public void deleteEvent(Long id) {
        Event event = eventRepo.findById(id).orElseThrow(()-> new EventNotFoundException("Event with id: " + id + " not found\n"));
        eventRepo.delete(event);
    }

    public Event updateEvent(Event ev) {
        return eventRepo.save(ev);
    }

    public Page<Event> getByQuery(Long id, Pageable pageable) {
        Page<Event> result = eventRepo.getByQuery(id, pageable);
        if(result.isEmpty()) throw new EventNotFoundException("No events found\n");
        else return result;
    }

}