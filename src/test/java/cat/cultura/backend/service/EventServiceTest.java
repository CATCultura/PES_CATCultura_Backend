package cat.cultura.backend.service;


import cat.cultura.backend.entity.Event;
import cat.cultura.backend.exceptions.EventAlreadyCreatedException;
import cat.cultura.backend.repository.EventJpaRepository;
import cat.cultura.backend.service.EventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

@SpringBootTest
class EventServiceTest {

    @Autowired
    EventService eventService;

    @MockBean
    EventJpaRepository eventJpaRepository;

    @Test
    void saveEventTestAllOk() {
        Event ev1 = new Event();
        ev1.setDenominacio("Concert de primavera");
        ev1.setDataInici("Dimarts");
        ev1.setUbicacio("Barcelona");
        ev1.setAdreca("C/ Quinta Forca");
        ev1.setEspai("Sideral");

        given(eventJpaRepository.save(ev1)).willReturn(ev1);

        Event result = eventService.saveEvent(ev1);
        Assertions.assertEquals(ev1, result);
    }

    @Test
    void saveEventTestRepeated() {
        Event ev1 = new Event();
        ev1.setDenominacio("Concert de primavera");
        ev1.setDataInici("Dimarts");
        ev1.setUbicacio("Barcelona");
        ev1.setAdreca("C/ Quinta Forca");
        ev1.setEspai("Sideral");

        List<Event> eventList = new ArrayList<>();

        eventList.add(ev1);

        Event ev2 = new Event();
        ev2.setDenominacio("Concert de primavera");
        ev2.setDataInici("Dimarts");
        ev2.setUbicacio("Barcelona");
        ev2.setAdreca("C/ Quinta Forca");
        ev2.setEspai("Sideral");


        given(eventJpaRepository.findByDenominacioLikeIgnoreCaseAllIgnoreCase(ev2.getDenominacio())).willReturn(eventList);

        Assertions.assertThrows(EventAlreadyCreatedException.class, ()-> eventService.saveEvent(ev2));
    }

    @Test
    void saveEventTestSameDenominacioDifferentAdreca() {
        Event ev1 = new Event();
        ev1.setDenominacio("Concert de primavera");
        ev1.setDataInici("Dimarts");
        ev1.setUbicacio("Barcelona");
        ev1.setAdreca("C/ Quinta Forca");
        ev1.setEspai("Sideral");

        List<Event> eventList = new ArrayList<>();

        eventList.add(ev1);

        Event ev2 = new Event();
        ev2.setDenominacio("Concert de primavera");
        ev2.setDataInici("Dimarts");
        ev2.setUbicacio("Barcelona");
        ev2.setAdreca("C/ Quarta Forca");
        ev2.setEspai("Sideral");


        given(eventJpaRepository.findByDenominacioLikeIgnoreCaseAllIgnoreCase(ev2.getDenominacio())).willReturn(eventList);
        given(eventJpaRepository.save(ev2)).willReturn(ev2);

        Event res = eventService.saveEvent(ev2);

        Assertions.assertEquals(res,ev2);
    }

    @Test
    void testSaveSeveralEventsAllOk() {
        Event ev1 = new Event();
        ev1.setDenominacio("Concert de primavera");
        ev1.setDataInici("Dimarts");
        ev1.setUbicacio("Barcelona");
        ev1.setAdreca("C/ Quinta Forca");
        ev1.setEspai("Sideral");

        Event ev2 = new Event();
        ev2.setDenominacio("Concert de primavera");
        ev2.setDataInici("Dimarts");
        ev2.setUbicacio("Barcelona");
        ev2.setAdreca("C/ Quarta Forca");
        ev2.setEspai("Sideral");

        List<Event> eventList = new ArrayList<>();
        eventList.add(ev1);
        eventList.add(ev2);

        given(eventJpaRepository.saveAll(eventList)).willReturn(eventList);

        List<Event> res = eventService.saveEvents(eventList);

        Assertions.assertEquals(res,eventList);
    }

    @Test
    void testSaveSeveralEventsRepeated() {
        Event ev1 = new Event();
        ev1.setDenominacio("Concert de primavera");
        ev1.setDataInici("Dimarts");
        ev1.setUbicacio("Barcelona");
        ev1.setAdreca("C/ Quinta Forca");
        ev1.setEspai("Sideral");

        List<Event> eventList = new ArrayList<>();
        eventList.add(ev1);

        Event ev2 = new Event();
        ev2.setDenominacio("Concert de tardor");
        ev2.setDataInici("Dimarts");
        ev2.setUbicacio("Barcelona");
        ev2.setAdreca("C/ Quarta Forca");
        ev2.setEspai("Sideral");

        Event ev3 = new Event();
        ev3.setDenominacio("Concert de primavera");
        ev3.setDataInici("Dimarts");
        ev3.setUbicacio("Barcelona");
        ev3.setAdreca("C/ Quinta Forca");
        ev3.setEspai("Sideral");

        List<Event> eventsToInsert = new ArrayList<>();
        eventsToInsert.add(ev1);
        eventsToInsert.add(ev2);

        given(eventJpaRepository.findByDenominacioLikeIgnoreCaseAllIgnoreCase(ev3.getDenominacio())).willReturn(eventList);

        Assertions.assertThrows(EventAlreadyCreatedException.class, ()-> eventService.saveEvents(eventsToInsert));

    }

    @Test
    void testUpdateEventOk() {
        Event old_event = new Event();
        old_event.setDenominacio("Concert de primavera");
        old_event.setDataInici("Dimarts");
        old_event.setUbicacio("Barcelona");
        old_event.setAdreca("C/ Quinta Forca");
        old_event.setEspai("Sideral");

        Event ev2 = new Event();
        ev2.setDenominacio("Concert de primavera");
        ev2.setDataInici("Dimarts");
        ev2.setUbicacio("Barcelona");
        ev2.setAdreca("C/ Quarta Forca");
        ev2.setEspai("Sideral");

        List<Event> eventList = new ArrayList<>();

        eventList.add(old_event);

        given(eventJpaRepository.findByDenominacioLikeIgnoreCaseAllIgnoreCase(ev2.getDenominacio())).willReturn(eventList);

        given(eventJpaRepository.save(ev2)).willReturn(ev2);

        ev2.setDenominacio("Concert de tardor");

        Event updated = eventService.updateEvent(ev2);

        Assertions.assertEquals(ev2,updated);
    }

    @Test
    void testUpdateEventRepeated() {
        Event old_event = new Event();
        old_event.setDenominacio("Concert de primavera");
        old_event.setDataInici("Dimarts");
        old_event.setUbicacio("Barcelona");
        old_event.setAdreca("C/ Quinta Forca");
        old_event.setEspai("Sideral");

        Event ev2 = new Event();
        ev2.setDenominacio("Concert de primavera");
        ev2.setDataInici("Dimarts");
        ev2.setUbicacio("Barcelona");
        ev2.setAdreca("C/ Quarta Forca");
        ev2.setEspai("Sideral");

        List<Event> eventList = new ArrayList<>();

        eventList.add(old_event);

        given(eventJpaRepository.findByDenominacioLikeIgnoreCaseAllIgnoreCase(ev2.getDenominacio())).willReturn(eventList);

        ev2.setAdreca("C/ Quinta Forca");


        Assertions.assertThrows(EventAlreadyCreatedException.class, () -> eventService.updateEvent(ev2));
    }

}
