package cat.cultura.backend.controller;

import cat.cultura.backend.dtos.EventDto;
import cat.cultura.backend.entity.Event;

import cat.cultura.backend.exceptions.EventNotFoundException;
import cat.cultura.backend.service.EventService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/events")
    public ResponseEntity<List<EventDto>> addEvent(@RequestBody List<EventDto> ev) {
        List<Event> eventsEntities = new ArrayList<>();
        for (EventDto eventDto : ev) {
            Event event = convertEventDtoToEntity(eventDto);
            eventsEntities.add(event);
        }
        List<Event> events = eventService.saveEvents(eventsEntities);
        return ResponseEntity.status(HttpStatus.CREATED).body(events.stream().map(this::convertEventToDto).collect(Collectors.toList()));
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventDto>> getEventsByQuery(
            @RequestParam(value = "id", required = false) Long id,
            Pageable pageable
    ) {
        Page<Event> events = eventService.getByQuery(id, pageable);
        if(events.isEmpty()) throw new EventNotFoundException();
        return ResponseEntity.status(HttpStatus.OK).body(events.stream().map(this::convertEventToDto).collect(Collectors.toList()));
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        return ResponseEntity.status(HttpStatus.OK).body(convertEventToDto(event));
    }

    @PutMapping("/events")
    public ResponseEntity<EventDto> updateEvent(@RequestBody EventDto ev) {
        Event eventEntity = convertEventDtoToEntity(ev);
        Event event = eventService.updateEvent(eventEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertEventToDto(event));
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id){
        eventService.deleteEvent(id);
        return ResponseEntity.status(HttpStatus.OK).body("Event with id " + id + " deleted.");
    }

    private EventDto convertEventToDto(Event event) {
        EventDto eventDto = modelMapper.map(event, EventDto.class);
        //....modifications....
        return eventDto;
    }

    private Event convertEventDtoToEntity(EventDto eventDto) {
        Event event = modelMapper.map(eventDto, Event.class);
        //....modifications....
        return event;
    }

}
