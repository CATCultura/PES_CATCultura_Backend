package cat.cultura.backend.service;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.exceptions.EventAlreadyCreatedException;
import cat.cultura.backend.exceptions.EventNotFoundException;
import cat.cultura.backend.exceptions.ForbiddenActionException;
import cat.cultura.backend.interceptors.CurrentUserAccessor;
import cat.cultura.backend.remoterequests.SimilarityServiceAdapter;
import cat.cultura.backend.remoterequests.SimilarityServiceImpl;
import cat.cultura.backend.repository.EventJpaRepository;
import cat.cultura.backend.utils.Score;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class EventService {

    private final EventJpaRepository eventRepo;

    private final SimilarityServiceAdapter similarityService;

    private final TagService tagService;

    private final CurrentUserAccessor currentUserAccesor;

    private boolean isUniqueInDB(Event ev) {
        List<Event> sameDenominacioEvents = eventRepo.findByDenominacioLikeIgnoreCaseAllIgnoreCase(ev.getDenominacio());
        if (sameDenominacioEvents.isEmpty()) return true;
        for (Event e : sameDenominacioEvents) {
            if (ev.equals(e)) return false;
        }
        return true;
    }

    public EventService(EventJpaRepository eventRepo,
                        TagService tagService,
                        SimilarityServiceImpl semanticService,
                        CurrentUserAccessor currentUserAccessor) {
        this.eventRepo = eventRepo;
        this.tagService = tagService;
        this.similarityService = semanticService;
        this.currentUserAccesor = currentUserAccessor;
    }

    public Event saveEvent(Event ev) {
        if (isUniqueInDB(ev)) {
            ev.setTagsAmbits(tagService.persistTags(ev.getTagsAmbits()));
            ev.setTagsCateg(tagService.persistTags(ev.getTagsCateg()));
            ev.setTagsAltresCateg(tagService.persistTags(ev.getTagsAltresCateg()));
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
        return eventRepo.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepo.findById(id).orElseThrow(EventNotFoundException::new);
    }

    public Event getEventByCodi(Long codi) {
        return eventRepo.findByCodi(codi).orElseThrow(EventNotFoundException::new);
    }

    public void deleteEvent(Long id) {
        Event event = eventRepo.findById(id).orElseThrow(EventNotFoundException::new);
        if (event.getOrganizer() == null) throw new ForbiddenActionException();
        if (!Objects.equals(currentUserAccesor.getCurrentUsername(),event.getOrganizer().getUsername())) {
            throw new ForbiddenActionException();
        }
        eventRepo.delete(event);
    }

    public Event updateEvent(Event ev) {
        if(eventRepo.findById(ev.getId()).isEmpty()) throw new EventNotFoundException();
        return eventRepo.save(ev);
    }

    public Page<Event> getByQuery(Long id, Pageable pageable) {
        return eventRepo.getByQuery(id, pageable);
    }

    public Page<Event> getBySemanticSimilarity(String query) {
        List<Score> queryResult;
        try {
             queryResult = similarityService.getMostSimilar(query);
        } catch (IOException e) {
            return null;
        }
        if (queryResult!= null) {
            List<Event> results = new ArrayList<>();
            for (Score id : queryResult) {
                if (id.getSimilarityScore() > 0.5)
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

    public String getAttendanceCode(Long eventId) {
        Event event = eventRepo.findById(eventId).orElseThrow(EventNotFoundException::new);
        return event.getAttendanceCode();
    }

    public String generateAttendanceCode(Long eventId) {
        Event event = eventRepo.findById(eventId).orElseThrow(EventNotFoundException::new);
        String code = event.generateAttendanceCode();
        eventRepo.save(event);
        return code;
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelEvent(Long eventId) {
        Event event = eventRepo.findById(eventId).orElseThrow(EventNotFoundException::new);
        if (event.getOrganizer() == null) throw new ForbiddenActionException();
        if (!Objects.equals(currentUserAccesor.getCurrentUsername(),event.getOrganizer().getUsername())) {
            throw new ForbiddenActionException();
        }
        event.setCancelado(true);
    }
}