package cat.cultura.backend.controller;

import cat.cultura.backend.dtos.EventDto;
import cat.cultura.backend.dtos.ReviewDto;
import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.Organizer;
import cat.cultura.backend.entity.Review;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.EventAlreadyCreatedException;
import cat.cultura.backend.interceptors.CurrentUserAccessor;
import cat.cultura.backend.mappers.EventMapper;
import cat.cultura.backend.mappers.ReviewMapper;
import cat.cultura.backend.service.EventService;
import cat.cultura.backend.service.RouteDataService;
import cat.cultura.backend.service.TagService;
import cat.cultura.backend.service.user.ReviewService;
import cat.cultura.backend.service.user.UserService;
import cat.cultura.backend.service.user.UserTrophyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private RouteDataService routeDataService;

    @Autowired
    private CurrentUserAccessor currentUserAccessor;

    @Autowired
    private UserService userService;

    @Autowired
    private UserTrophyService userTrophyService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private TagService tagService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/events")
    public ResponseEntity<List<EventDto>> addEvent(@RequestBody List<EventDto> ev) {
        List<Event> eventsEntities = new ArrayList<>();
        User u = userService.getUserByUsername(currentUserAccessor.getCurrentUsername());
        logger.info("Received request for creating a new event." +
                "Requester is user with id {} and name {}", u.getId(), u.getUsername());
        for (EventDto eventDto : ev) {
            if (eventDto.getId() != null) {
                logger.info("Event has an externally-assigned ID, but ID is assigned automatically." +
                        "Returning 409 error.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
            }
            Event event = eventMapper.convertEventDtoToEntity(eventDto);
            event.setOrganizer((Organizer) u);
            eventsEntities.add(event);
        }
        List<Event> events;
        try {
            events = eventService.saveEvents(eventsEntities);
        } catch (EventAlreadyCreatedException eac) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(events.stream().map(eventMapper::convertEventToDto).toList());
    }

    @GetMapping(value = "/events", params = {"q"})
    public ResponseEntity<List<List<EventDto>>> getEventsByNLQuery(
            @RequestParam(value = "q", required = true) String query
    ) {
        List<List<EventDto>> result = new LinkedList<>();
        logger.info("Request for events that match the query {}.", query);
        List<List<Event>> events = eventService.getBySemanticSimilarity(query);
        result.add(events.get(0).stream().map(eventMapper::convertEventToDto).toList());
        result.add(events.get(1).stream().map(eventMapper::convertEventToDto).toList());
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }


    @GetMapping("/events")
    public ResponseEntity<List<EventDto>> getEventsByQuery(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "tag", required = false) String tag,
//            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "organizer", required = false) Long orgId,
            Pageable pageable
    ) {
        logger.info("Request for events.");
        Page<Event> events;
        if (tag != null) {
            logger.info("Request for events with tag {}.", tag);
            events = new PageImpl<>(tagService.getTagByName(tag).getEventList().stream().toList().subList(pageable.getPageNumber()*pageable.getPageSize(),
                    (pageable.getPageNumber()+1)*pageable.getPageSize()),pageable, pageable.getPageSize());
        }
        else {
            events = eventService.getByQuery(id, pageable);
        }
        if (events == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        return ResponseEntity.status(HttpStatus.OK).body(events.stream().map(eventMapper::convertEventToDto).toList());
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        return ResponseEntity.status(HttpStatus.OK).body(eventMapper.convertEventToDto(event));
    }

    @PutMapping("/events")
    public ResponseEntity<EventDto> updateEvent(@RequestBody EventDto ev) {
        logger.info("Request for updating event with id {} and title {}", ev.getId(), ev.getDenominacio());
        if (ev.getId() == null)
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        Event eventEntity = eventMapper.convertEventDtoToEntity(ev);
        if (eventEntity.getOrganizer() == null) {
            logger.info("Event doesn't have an organizer, so it cannot be edited. Returning 403");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        if (! Objects.equals(currentUserAccessor.getCurrentUsername(), eventEntity.getOrganizer().getUsername())) {
            logger.info("Only the organizer can edit an event. Returning 403");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        Event event = eventService.updateEvent(eventEntity);
        logger.info("Event updated.");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(eventMapper.convertEventToDto(event));
    }

    @PutMapping("/events/{eventId}/cancelled")
    public ResponseEntity<EventDto> cancelEvent(@PathVariable Long eventId) {
        logger.info("Received request for cancelling event with id {}", eventId);
        eventService.cancelEvent(eventId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }



    @DeleteMapping("/events/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id){
        logger.info("Received request for deleting event with id {}", id);
        eventService.deleteEvent(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/events/outdated")
    public ResponseEntity<List<EventDto>> updateEvent() {
        List<Event> pe = eventService.updateEvents();
        return ResponseEntity.status(HttpStatus.CREATED).body(pe.stream().map(eventMapper::convertEventToDto).toList());
    }

    @GetMapping("/events/outdated")
    public ResponseEntity<List<EventDto>> getPastEvents() {
        List<Event> pe = eventService.getPastEvents();
        return ResponseEntity.status(HttpStatus.OK).body(pe.stream().map(eventMapper::convertEventToDto).toList());
    }

    @GetMapping("/events/{id}/reviews")
    public ResponseEntity<List<ReviewDto>> getEventsByQuery(@PathVariable Long id) {
        List<Review> reviews;
        try {
            reviews = reviewService.getReviewsByEvent(id);
        } catch (AssertionError as) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(reviews.stream().map(reviewMapper::convertReviewToDto).toList());
    }

    @GetMapping("/events/{id}/attendanceCode")
    public ResponseEntity<String> getAttendanceCode(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getAttendanceCode(id));
    }

    @PostMapping("/events/{id}/attendanceCode")
    public ResponseEntity<String> generateAttendanceCode(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.generateAttendanceCode(id));
    }


}
