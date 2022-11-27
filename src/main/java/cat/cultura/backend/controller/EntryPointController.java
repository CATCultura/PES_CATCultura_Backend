package cat.cultura.backend.controller;

import cat.cultura.backend.dtos.EventDto;
import cat.cultura.backend.entity.Event;
import cat.cultura.backend.exceptions.EventAlreadyCreatedException;
import cat.cultura.backend.exceptions.EventNotFoundException;
import cat.cultura.backend.exceptions.MissingRequiredParametersException;
import cat.cultura.backend.service.EventService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EntryPointController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ModelMapper modelMapper;


    @PostMapping("/insert")
    public ResponseEntity<String> updateDB(@RequestHeader("auth-token") String authToken, @RequestBody List<EventDto> ev) {
        if (authToken.equals("my-hash")) {
            List<Event> eventsEntities = new ArrayList<>();
            for (EventDto eventDto : ev) {
                Event event = null;
                try {
                    event = convertEventDtoToEntity(eventDto);
                }
                catch (MissingRequiredParametersException ignored) {

                }
                if (event != null)
                    eventsEntities.add(event);
            }
            for (Event e: eventsEntities) {
                try {
                    eventService.saveEvent(e);
                }
                catch (EventAlreadyCreatedException ignored) {

                }
            }
            return new ResponseEntity<>("All ok", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("You've done fucked up", HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/allevents")
    public ResponseEntity<List<EventDto>> getAllEvents() {
        List<Event> allEvents = eventService.getEvents();
        if(allEvents.isEmpty()) throw new EventNotFoundException();
        return ResponseEntity.status(HttpStatus.OK).body(allEvents.stream().map(this::convertEventToDto).toList());
    }


    private EventDto convertEventToDto(Event event) {
        return modelMapper.map(event, EventDto.class);
    }

    private Event convertEventDtoToEntity(EventDto eventDto) {
        if (eventDto.getDenominacio() == null) throw new MissingRequiredParametersException();
        if (eventDto.getDataInici() == null) throw new MissingRequiredParametersException();
        if (eventDto.getUbicacio() == null) throw new MissingRequiredParametersException();
        //....modifications....
        return modelMapper.map(eventDto, Event.class);
    }
}
