package cat.cultura.backend.controller;

import cat.cultura.backend.dtos.EventDto;
import cat.cultura.backend.entity.Event;
import cat.cultura.backend.service.EventService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EventController {
    @Autowired
    private EventService service;

    @Autowired
    MessageSource messageSource;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/event")
    public EventDto addEvent(@RequestBody EventDto ev) throws ParseException {
        Event eventEntity = convertEventDtoToEntity(ev);
        Event event = service.saveEvent(eventEntity);
        return convertEventToDto(event);
    }

    @PostMapping("/events")
    public List<EventDto> addEvent(@RequestBody List<EventDto> ev) throws ParseException{
        List<Event> eventsEntities = new ArrayList<>();
        for (EventDto eventDto : ev) {
            Event event = convertEventDtoToEntity(eventDto);
            eventsEntities.add(event);
        }
        List<Event> events = service.saveEvents(eventsEntities);
        return events.stream().map(this::convertEventToDto).collect(Collectors.toList());
    }

    @GetMapping("/events")
    public List<EventDto> findAllEvents() {
        List<Event> events = service.getEvents();
        return events.stream().map(this::convertEventToDto).collect(Collectors.toList());
    }

    @GetMapping("/events/{id}")
    public EventDto findEventById(@PathVariable Long id) {
        Event event = service.getEventByID(id);
        return convertEventToDto(event);
    }

    @PutMapping("/events")
    public EventDto updateEvent(@RequestBody EventDto ev) throws ParseException {
        Event eventEntity = convertEventDtoToEntity(ev);
        Event event = service.updateEvent(eventEntity);
        return convertEventToDto(event);
    }

    @DeleteMapping("/events/{id}")
    public String deleteEvent(@PathVariable Long id){
        return service.deleteEvent(id);
    }


    private EventDto convertEventToDto(Event event) {
        EventDto eventDto = modelMapper.map(event, EventDto.class);
        //....modifications....
        return eventDto;
    }

    private Event convertEventDtoToEntity(EventDto eventDto) throws ParseException {
        Event event = modelMapper.map(eventDto, Event.class);
        //....modifications....
        return event;
    }
}
