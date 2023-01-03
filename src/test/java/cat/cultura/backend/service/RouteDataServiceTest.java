package cat.cultura.backend.service;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.repository.EventJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class RouteDataServiceTest {
    @Autowired
    RouteDataService routeDataService;

    @MockBean
    EventJpaRepository eventRepo;

    @MockBean
    UserJpaRepository userRepo;

    private Event event1 = new Event();
    private Event event2 = new Event();
    private Event event3 = new Event();

    private List<Event> events = new ArrayList<>();

    @BeforeEach
    void declarationOfEvents() {
        event1.setId(1L);
        event1.setDenominacio("1L");
        event1.setDataInici("XXX");
        event1.setUbicacio("XXXXX");
        event1.setLatitud(41);
        event1.setLatitud(2);

        event2.setId(2L);
        event2.setDenominacio("1LL");
        event2.setDataInici("XXX");
        event2.setUbicacio("XXXXX");
        event2.setLatitud(42);
        event2.setLatitud(3);

        event3.setId(3L);
        event3.setDenominacio("1LLL");
        event3.setDataInici("XXX");
        event3.setUbicacio("XXXXX");
        event3.setLatitud(50);
        event3.setLatitud(3);

        events.add(event2);
        events.add(event3);
        events.add(event1);
    }

    @Test
    void getRouteInADayAndLocationTest() {
        List<Long> discardedEvents = new ArrayList<>();
        discardedEvents.add(event2.getId());

        List<Event> orderedEvents = new ArrayList<>();
        orderedEvents.add(event1);
        orderedEvents.add(event3);

        User user = new User();
        user.setId(4L);

        given(userRepo.findById(4L)).willReturn(Optional.of(user));
        given(eventRepo.geByDayAndLocation(anyString(),anyDouble(),anyDouble(),anyDouble(),anyDouble())).willReturn(events);
        List<Event> actualEvents = routeDataService.getRouteInADayAndLocation(anyDouble(),anyDouble(),anyInt(),anyString(),eq(4L),discardedEvents);
        assertEquals(orderedEvents, actualEvents);
    }

    @Test
    void getRouteInADayAndLocation_NoDiscardedEvents_Test() {
        List<Long> discardedEvents = new ArrayList<>();
        discardedEvents.add(event2.getId());

        List<Event> orderedEvents = new ArrayList<>();
        orderedEvents.add(event1);
        orderedEvents.add(event3);

        User user = new User();
        user.setId(4L);

        given(userRepo.findById(4L)).willReturn(Optional.of(user));
        given(eventRepo.geByDayAndLocation(anyString(),anyDouble(),anyDouble(),anyDouble(),anyDouble())).willReturn(events);
        List<Event> actualEvents = routeDataService.getRouteInADayAndLocation(anyDouble(),anyDouble(),anyInt(),anyString(),eq(4L),discardedEvents);
        assertEquals(orderedEvents, actualEvents);
    }

    @Test
    void getRouteInADayAndLocation_NoUser_Test() {
        List<Long> discardedEvents = new ArrayList<>();
        discardedEvents.add(event2.getId());

        List<Event> orderedEvents = new ArrayList<>();
        orderedEvents.add(event1);
        orderedEvents.add(event3);

        given(eventRepo.geByDayAndLocation(anyString(),anyDouble(),anyDouble(),anyDouble(),anyDouble())).willReturn(events);
        List<Event> actualEvents = routeDataService.getRouteInADayAndLocation(anyDouble(),anyDouble(),anyInt(),anyString(),eq(null),discardedEvents);
        assertEquals(orderedEvents, actualEvents);
    }

    @Test
    void getRouteInADayAndLocation_NoUser_NoDiscardedEvents_Test() {
        List<Event> orderedEvents = new ArrayList<>();
        orderedEvents.add(event1);
        orderedEvents.add(event2);
        orderedEvents.add(event3);

        given(eventRepo.geByDayAndLocation(anyString(),anyDouble(),anyDouble(),anyDouble(),anyDouble())).willReturn(events);
        List<Event> actualEvents = routeDataService.getRouteInADayAndLocation(anyDouble(),anyDouble(),anyInt(),anyString(),eq(null),null);
        assertEquals(orderedEvents, actualEvents);
    }

}
