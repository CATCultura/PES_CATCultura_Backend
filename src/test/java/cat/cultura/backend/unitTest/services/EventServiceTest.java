package cat.cultura.backend.unitTest.services;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.EventNotFoundException;
import cat.cultura.backend.repository.EventJpaRepository;
import cat.cultura.backend.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class EventServiceTest {

    @Autowired
    private EventService eventService;

    @MockBean
    private EventJpaRepository eventRepo;

    @Test
    public void saveEventsTest() throws Exception {
        Event event = new Event();
        event.setId(3L);
        List<Event> events = new ArrayList<>();
        events.add(event);
        given(eventRepo.saveAll(events)).willReturn(events);

        List<Event> result = eventService.saveEvents(events);
        assertEquals(events, result);
    }

    @Test
    public void getEventByIdTest() throws Exception {
        Event event = new Event();
        event.setId(3L);
        given(eventRepo.findById(3L)).willReturn(Optional.of(event));

        Event result = eventService.getEventById(3L);
        assertEquals(event, result);
    }

    @Test
    public void getEventByIdIfTheEventDoesNotExistTest() throws Exception {
        given(eventRepo.findById(3L)).willReturn(Optional.empty());

        assertThrows(
                EventNotFoundException.class,
                ()->eventService.getEventById(2L)
        );
    }

    @Test
    public void updateEventTest() throws Exception {
        Event event = new Event();
        event.setId(3L);
        given(eventRepo.save(event)).willReturn(event);

        Event result = eventService.updateEvent(event);
        assertEquals(event, result);
    }

    @Test
    public void getEventsByQueryIdTest() throws Exception {
        Event event = new Event();
        event.setId(2L);
        List<Event> events = new ArrayList<>();
        events.add(event);
        Page<Event> page = new PageImpl<>(events);
        Pageable pageable = PageRequest.of(0, 20);
        given(eventRepo.getByQuery(2L, pageable)).willReturn(page);

        Page<Event> result = eventService.getByQuery(2l,pageable);
        assertEquals(page, result);
    }

    @Test
    public void getEventsByQueryIdIfNoEventsFoundTest() throws Exception {
        List<Event> events = new ArrayList<>();
        Page<Event> page = new PageImpl<>(events);
        Pageable pageable = PageRequest.of(0, 20);
        given(eventRepo.getByQuery(2L, pageable)).willReturn(page);

        assertThrows(
                EventNotFoundException.class,
                ()->eventService.getByQuery(2l,pageable)
        );
    }

}
