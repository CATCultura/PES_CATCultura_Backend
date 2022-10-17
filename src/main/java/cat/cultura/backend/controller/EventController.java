package cat.cultura.backend.controller;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
public class EventController {
    @Autowired
    private EventService service;

    @Autowired
    MessageSource messageSource;

    @PostMapping("/event")
    public Event addEvent(@RequestBody Event ev) {
        return service.saveEvent(ev);
    }

    @PostMapping("/events")
    public List<Event> addEvent(@RequestBody List<Event> ev) {
        return service.saveEvents(ev);
    }

    @GetMapping("/events")
    public List<Event> findAllEvents() {
        return service.getEvents();
    }

    @GetMapping("/events/id={id}")
    public Event findEventById(@PathVariable Long id) {
        return service.getEventByID(id);
    }

    @PutMapping("/events")
    public Event updateEvent(@RequestBody Event ev) {
        return service.updateEvent(ev);
    }

    @DeleteMapping("/events/{id}")
    public String deleteEvent(@PathVariable Long id){
        return service.deleteEvent(id);
    }
}
