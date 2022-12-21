package cat.cultura.backend.service;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.tag.Tag;
import cat.cultura.backend.exceptions.EventAlreadyCreatedException;
import cat.cultura.backend.exceptions.EventNotFoundException;
import cat.cultura.backend.repository.EventJpaRepository;
import cat.cultura.backend.repository.TagJpaRepository;
import cat.cultura.backend.utils.Score;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    private final EventJpaRepository eventRepo;

    private final TagJpaRepository tagRepo;

    private final SemanticService semanticService;

    private boolean isUniqueInDB(Event ev) {
        List<Event> sameDenominacioEvents = eventRepo.findByDenominacioLikeIgnoreCaseAllIgnoreCase(ev.getDenominacio());
        if (sameDenominacioEvents.isEmpty()) return true;
        for (Event e : sameDenominacioEvents) {
            if (ev.equals(e)) return false;
        }
        return true;
    }

    private List<Tag> persistTags(List<Tag> tags) {
        List<Tag> res = new ArrayList<>();
        for (Tag t : tags) {
            if (!tagRepo.existsByValue(t.getValue())) {
                res.add(tagRepo.save(t));
            }
            else {
                res.add(tagRepo.findByValue(t.getValue()).orElse(null));
            }
        }
        return res;
    }

    public EventService(EventJpaRepository eventRepo, TagJpaRepository tagRepo, SemanticService semanticService) {
        this.eventRepo = eventRepo;
        this.tagRepo = tagRepo;
        this.semanticService = semanticService;
    }

    public Event saveEvent(Event ev) {
        if (isUniqueInDB(ev)) {
            ev.setTagsAmbits(persistTags(ev.getTagsAmbits()));
            ev.setTagsCateg(persistTags(ev.getTagsCateg()));
            ev.setTagsAltresCateg(persistTags(ev.getTagsAltresCateg()));
            return eventRepo.save(ev);
        }
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

    public Page<Event> getBySemanticSimilarity(String query) {
        List<Score> queryResult;
        try {
             queryResult = semanticService.getEventListByQuery(query);
        } catch (IOException e) {
            return null;
        }
        if (queryResult!= null) {
            List<Event> results = new ArrayList<>();
            for (Score id : queryResult) {
                eventRepo.findById(id.getEventId()).ifPresent(results::add);
            }
            return new PageImpl<>(results);
        }
        return new PageImpl<>(new ArrayList<>());
    }
    public List<Event> updateEvents() {
        List<Event> events = eventRepo.findAll();
        List<Event> updatedEvents = new ArrayList<>();
        for(Event e : events) {
            if(e.isPastEvent()) {
                e.setOutdated(true);
                updatedEvents.add(e);
            }
        }
        return eventRepo.saveAll(updatedEvents);
    }

    public List<Event> getPastEvents() {
        return eventRepo.getPastEvents();
    }

}