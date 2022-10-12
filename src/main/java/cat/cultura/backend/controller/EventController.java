package cat.cultura.backend.controller;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventController {
    @Autowired
    private EventService service;

    @PostMapping("/addEvent")
    public Event addEvent(@RequestBody Event ev) {
        return service.saveEvent(ev);
    }

    @PostMapping("/addEvents")
    public List<Event> addEvent(@RequestBody List<Event> ev) {
        return service.saveEvents(ev);
    }

    @GetMapping("/events")
    public List<Event> findAllEvents() {
        return service.getEvents();
    }

    @GetMapping("/event/id={id}")
    public Event findEventById(@PathVariable Long id) {
        return service.getEventByID(id);
    }

    @GetMapping("/event/codi={codi}")
    public Event findEventByCodi(@PathVariable Long codi) {
        return service.getEventByCodi(codi);
    }

    @PutMapping("/updateEvent")
    public Event updateEvent(@RequestBody Event ev) {
        return service.updateEvent(ev);
    }

    @DeleteMapping("/deleteEvent/id={id}")
    public String deleteEvent(@PathVariable Long id){
        return service.deleteEvent(id);
    }
}
