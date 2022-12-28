package cat.cultura.backend.controller;

import cat.cultura.backend.dtos.EventDto;
import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.Organizer;
import cat.cultura.backend.entity.Role;
import cat.cultura.backend.exceptions.EventAlreadyCreatedException;
import cat.cultura.backend.exceptions.EventNotFoundException;
import cat.cultura.backend.exceptions.MissingRequiredParametersException;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.mappers.EventMapper;
import cat.cultura.backend.service.EventService;
import cat.cultura.backend.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EntryPointController {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @PostMapping("/insert")
    public ResponseEntity<String> updateDB(@RequestBody List<EventDto> ev) {
        List<Event> eventEntities = new ArrayList<>();
        for (EventDto eventDto : ev) {
            Event event = null;
            try {
                event = eventMapper.convertEventDtoToEntity(eventDto);
            }
            catch (MissingRequiredParametersException ignored) {
                logger.info(String.format("Missing parameter for dto with name: %s",eventDto.getDenominacio()));
            }
            if (event != null)
                eventEntities.add(event);
        }
        for (Event e: eventEntities) {
            if (e.getOrganizer() != null) {
                try {
                    e.setOrganizer( (Organizer) userService.getUserByUsername(e.getOrganizer().getUsername(), Role.ORGANIZER));
                }
                catch (UserNotFoundException ex) {
                    userService.createOrganizer(e.getOrganizer());
                    logger.info(String.format("Created new organizer for event %s",e.getDenominacio()));
                }
            }
            try {
                eventService.saveEvent(e);
                logger.info("Saved event {}",e.getDenominacio());
            }
            catch (EventAlreadyCreatedException ignored) {
                logger.info(String.format("Event %s already created",e.getDenominacio()));
            }

        }
        logger.info("Insertion OK");
        return new ResponseEntity<>("All ok", HttpStatus.OK);
    }

    @GetMapping("/allevents")
    public ResponseEntity<List<EventDto>> getAllEvents() {
        List<Event> allEvents = eventService.getEvents();
        if (allEvents.isEmpty())
            throw new EventNotFoundException();
        List<EventDto> result = allEvents.stream().map(eventMapper::convertEventToDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


}
