package cat.cultura.backend.service;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.exceptions.EventAlreadyCreatedException;
import cat.cultura.backend.exceptions.EventNotFoundException;
import cat.cultura.backend.exceptions.ForbiddenActionException;
import cat.cultura.backend.interceptors.CurrentUserAccessor;
import cat.cultura.backend.remoterequests.SimilarityServiceAdapter;
import cat.cultura.backend.remoterequests.SimilarityServiceImpl;
import cat.cultura.backend.repository.EventJpaRepository;
import cat.cultura.backend.utils.Coordinate;
import cat.cultura.backend.utils.Score;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
public class EventService {

    private final EventJpaRepository eventRepo;

    private final SimilarityServiceAdapter similarityService;

    private final TagService tagService;

    private final CurrentUserAccessor currentUserAccesor;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
            logger.info("Saving event {}", ev.getDenominacio());
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

        Event event = eventRepo.findById(id).orElseThrow(()-> new EventNotFoundException("Event with id: " + id + " not found"));
        if (event.getOrganizer() == null) {
            logger.warn("Attempt to delete event {} ({}). Cannot be done because event doesn't have an organizer.",
                    event.getId(), event.getDenominacio());
            throw new ForbiddenActionException();
        }

        if (!Objects.equals(currentUserAccesor.getCurrentUsername(),event.getOrganizer().getUsername())) {
            logger.warn("Attempt to delete event {} ({}). Cannot be done because the user trying ({}) isn't the organizer.",
                    event.getId(), event.getDenominacio(), currentUserAccesor.getCurrentUsername());
            throw new ForbiddenActionException();
        }
        logger.info("Deleting event with {}", id);
        eventRepo.delete(event);
    }

    public Event updateEvent(Event ev) {
        if (eventRepo.existsById(ev.getId()))
            return eventRepo.save(ev);
        throw new EventNotFoundException();

    }

    public Page<Event> getByQuery(Long id, Pageable pageable) {
        return eventRepo.getByQuery(id, pageable);
    }
    public List<Event> getCloseEvents(Double lat, Double lon) {
        List<Coordinate> c = Coordinate.getQuadrantCoordinates(lon,lat,1500);
        List<Event> events = eventRepo.getByLocation(c.get(0).getLon(), c.get(1).getLon(), c.get(2).getLat(), c.get(3).getLat());
        if(events.size() > 40) return events.subList(0,41);
        else return events;
    }

    public Map<String,List<Event>> getBySemanticSimilarity(String query) {
        List<Score> queryResult;
        Map<String,List<Event>>  results = new HashMap<>();
        results.put("Similar", new ArrayList<>());
        results.put("Not similar", new ArrayList<>());
        try {
             queryResult = similarityService.getMostSimilar(query);
        } catch (IOException e) {
            return results;
        }
        if (queryResult!= null) {
            for (Score id : queryResult) {
                if (id.getSimilarityScore() > 0.5)
                    eventRepo.findById(id.getEventId()).ifPresent(results.get("Similar")::add);
                else eventRepo.findById(id.getEventId()).ifPresent(results.get("Not similar")::add);
            }
        }
        return results;
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

    public List<Event> getEventsByTag(String tag) {
        return tagService.getTagByName(tag).getEventList().
                stream().filter(event -> !event.isOutdated()).toList();
    }
}