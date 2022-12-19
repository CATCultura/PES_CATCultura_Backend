package cat.cultura.backend.controller;

import cat.cultura.backend.dtos.EventDto;
import cat.cultura.backend.dtos.ReviewDto;
import cat.cultura.backend.entity.Event;

import cat.cultura.backend.entity.Review;
import cat.cultura.backend.exceptions.EventAlreadyCreatedException;
import cat.cultura.backend.exceptions.MissingRequiredParametersException;
import cat.cultura.backend.mappers.EventMapper;
import cat.cultura.backend.mappers.ReviewMapper;
import cat.cultura.backend.service.*;
import cat.cultura.backend.service.user.UserService;
import cat.cultura.backend.service.user.UserTrophyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private RouteService routeService;

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

    @PostMapping("/events")
    public ResponseEntity<List<EventDto>> addEvent(@RequestBody List<EventDto> ev) {
        List<Event> eventsEntities = new ArrayList<>();
        for (EventDto eventDto : ev) {
            Event event;
            try {
                event = eventMapper.convertEventDtoToEntity(eventDto);
            }
            catch (MissingRequiredParametersException mpe) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
            }
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

    @GetMapping("/events")
    public ResponseEntity<List<EventDto>> getEventsByQuery(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "q", required = false) String query,
            Pageable pageable
    ) {
        Page<Event> events;
        if (tag != null) {
            events = new PageImpl<>(tagService.getTagByName(tag).getEventList().stream().toList().subList(pageable.getPageNumber()*pageable.getPageSize(),
                    (pageable.getPageNumber()+1)*pageable.getPageSize()),pageable, pageable.getPageSize());
        }
        else if (query != null) {
            events = eventService.getBySemanticSimilarity(query);
        }
        else {
            events = eventService.getByQuery(id, pageable);
        }

        return ResponseEntity.status(HttpStatus.OK).body(events.stream().map(eventMapper::convertEventToDto).toList());
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        return ResponseEntity.status(HttpStatus.OK).body(eventMapper.convertEventToDto(event));
    }

    @PutMapping("/events")
    public ResponseEntity<EventDto> updateEvent(@RequestBody EventDto ev) {
        Event eventEntity = eventMapper.convertEventDtoToEntity(ev);
        Event event = eventService.updateEvent(eventEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventMapper.convertEventToDto(event));
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id){
        eventService.deleteEvent(id);
        return ResponseEntity.status(HttpStatus.OK).build();
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
