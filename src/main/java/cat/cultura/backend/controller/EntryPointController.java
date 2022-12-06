package cat.cultura.backend.controller;

import cat.cultura.backend.dtos.EventDto;
import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.Organizer;
import cat.cultura.backend.entity.Role;
import cat.cultura.backend.exceptions.EventAlreadyCreatedException;
import cat.cultura.backend.exceptions.EventNotFoundException;
import cat.cultura.backend.exceptions.MissingRequiredParametersException;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.service.EventService;
import cat.cultura.backend.service.UserService;
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

    @Autowired
    private UserService userService;


    @PostMapping("/insert")
    public ResponseEntity<String> updateDB(@RequestBody List<EventDto> ev) {
        List<Event> eventEntities = new ArrayList<>();
        for (EventDto eventDto : ev) {
            Event event = null;
            try {
                event = convertEventDtoToEntity(eventDto);
            }
            catch (MissingRequiredParametersException ignored) {

            }
            if (event != null)
                eventEntities.add(event);
        }
        for (Event e: eventEntities) {
            if (e.getOrganizer() != null) {
                try {
                    e.setOrganizer((Organizer)userService.getUserByUsername(e.getOrganizer().getUsername(), Role.ORGANIZER));
                }
                catch (UserNotFoundException ex) {
                    userService.createOrganizer(e.getOrganizer());
                    continue;
                }
            }

            try {
                eventService.saveEvent(e);
            }
            catch (EventAlreadyCreatedException ignored) {

            }
        }
        return new ResponseEntity<>("All ok", HttpStatus.OK);
    }

    private Organizer extractOrganizerInfo(EventDto eventDto) {
        String nomOrganitzador = eventDto.getNomOrganitzador();
        String urlOrganitzador = eventDto.getUrlOrganitzador();
        String telefonOrganitzador = eventDto.getTelefonOrganitzador();
        String emailOrganitzador = eventDto.getEmailOrganitzador();
        if (nomOrganitzador != null && (urlOrganitzador != null || telefonOrganitzador != null || emailOrganitzador != null)) {
            Organizer org;
            org = new Organizer(nomOrganitzador);
            org.setUrl(urlOrganitzador);
            org.setTelefon(telefonOrganitzador);
            org.setEmail(emailOrganitzador);
            return org;
        }
        return null;

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
        Event result = modelMapper.map(eventDto, Event.class);
        Organizer org = extractOrganizerInfo(eventDto);
        result.setOrganizer(org);
        return result;
    }
}
