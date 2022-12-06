package cat.cultura.backend.controller;

import cat.cultura.backend.dtos.EventDto;
import cat.cultura.backend.entity.Event;
import cat.cultura.backend.exceptions.EventAlreadyCreatedException;
import cat.cultura.backend.exceptions.MissingRequiredParametersException;
import cat.cultura.backend.service.EventService;
import cat.cultura.backend.service.RouteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    private ModelMapper modelMapper;



    @PostMapping("/events")
    public ResponseEntity<List<EventDto>> addEvent(@RequestBody List<EventDto> ev) {
        List<Event> eventsEntities = new ArrayList<>();
        for (EventDto eventDto : ev) {
            Event event;
            try {
                event = convertEventDtoToEntity(eventDto);
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

        return ResponseEntity.status(HttpStatus.CREATED).body(events.stream().map(this::convertEventToDto).toList());
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventDto>> getEventsByQuery(
            @RequestParam(value = "id", required = false) Long id,
            Pageable pageable
    ) {
        Page<Event> events = eventService.getByQuery(id, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(events.stream().map(this::convertEventToDto).toList());
    }

    @GetMapping("/events/route")
    public ResponseEntity<List<EventDto>> getEventsRoute(
            @RequestParam(required = true) double lat,
            @RequestParam(required = true) double lon,
            @RequestParam(required = true) int radius,
            @RequestParam(required = true) String day1
    ) {
        List<Event> events = routeService.getRouteByQuery(lat, lon, radius, day1);
        return ResponseEntity.status(HttpStatus.OK).body(events.stream().map(this::convertEventToDto).toList());
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
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private EventDto convertEventToDto(Event event) {
        EventDto eventDto = modelMapper.map(event, EventDto.class);
        if (event.getOrganizer() != null) {
            eventDto.setEmailOrganitzador(event.getOrganizer().getEmail());
            eventDto.setUrlOrganitzador(event.getOrganizer().getUrl());
            eventDto.setTelefonOrganitzador(event.getOrganizer().getTelefon());
            eventDto.setNomOrganitzador(event.getOrganizer().getUsername());
            eventDto.setIdOrganitzador(event.getOrganizer().getId());
        }

        //....modifications....
        return eventDto;
    }

    private Event convertEventDtoToEntity(EventDto eventDto) {
        if (eventDto.getDenominacio() == null) throw new MissingRequiredParametersException();
        if (eventDto.getDataInici() == null) throw new MissingRequiredParametersException();
        if (eventDto.getUbicacio() == null) throw new MissingRequiredParametersException();
        //....modifications....
        return modelMapper.map(eventDto, Event.class);
    }

}
