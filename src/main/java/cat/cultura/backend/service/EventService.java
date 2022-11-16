package cat.cultura.backend.service;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.exceptions.EventAlreadyCreatedException;
import cat.cultura.backend.exceptions.EventNotFoundException;
import cat.cultura.backend.repository.EventJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.xml.stream.Location;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    private final EventJpaRepository eventRepo;

    private boolean isUniqueInDB(Event ev) {
        List<Event> sameDenominacioEvents = eventRepo.findByDenominacioLikeIgnoreCaseAllIgnoreCase(ev.getDenominacio());
        if (sameDenominacioEvents.isEmpty()) return true;
        for (Event e : sameDenominacioEvents) {
            if (ev.equals(e)) return false;
        }
        return true;
    }

    public EventService(EventJpaRepository eventRepo) {
        this.eventRepo = eventRepo;
    }

    public Event saveEvent(Event ev) {
        if (isUniqueInDB(ev)) return eventRepo.save(ev);
        throw new EventAlreadyCreatedException();
    }

    public List<Event> saveEvents(List<Event> evs) {
        for (Event e : evs) {
            if (!isUniqueInDB(e)) throw new EventAlreadyCreatedException();
        }
        return eventRepo.saveAll(evs);
    }

    public List<Event> getEvents() {
        List<Event> result = eventRepo.findAll();
        if(result.isEmpty()) throw new EventNotFoundException("No Events found");
        else return result;
    }

    public Event getEventById(Long id) {
        return eventRepo.findById(id).orElseThrow(()-> new EventNotFoundException("Event with id: " + id + " not found"));
    }

    public Event getEventByCodi(Long codi) {
        return eventRepo.findByCodi(codi).orElseThrow(()-> new EventNotFoundException("Event with code: " + codi + " not found"));
    }

    public void deleteEvent(Long id) {
        Event event = eventRepo.findById(id).orElseThrow(()-> new EventNotFoundException("Event with id: " + id + " not found"));
        eventRepo.delete(event);
    }

    public Event updateEvent(Event ev) {
        if (!isUniqueInDB(ev)) throw new EventAlreadyCreatedException();
        return eventRepo.save(ev);
    }

    public Page<Event> getByQuery(Long id, Pageable pageable) {
        Page<Event> result = eventRepo.getByQuery(id, pageable);
        if(result.isEmpty()) throw new EventNotFoundException("No events found");
        else return result;
    }

}