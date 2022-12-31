package cat.cultura.backend.service;


import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.Organizer;
import cat.cultura.backend.exceptions.EventAlreadyCreatedException;
import cat.cultura.backend.exceptions.ForbiddenActionException;
import cat.cultura.backend.interceptors.CurrentUserAccessor;
import cat.cultura.backend.remoterequests.SimilarityServiceImpl;
import cat.cultura.backend.repository.EventJpaRepository;
import cat.cultura.backend.service.EventService;
import cat.cultura.backend.utils.Score;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.io.IOException;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class EventServiceTest {

    @Autowired
    EventService eventService;

    @MockBean
    EventJpaRepository eventJpaRepository;

    @MockBean
    SimilarityServiceImpl similarityService;

    @MockBean
    CurrentUserAccessor currentUserAccessor;


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
        old_event.setId(123L);
        old_event.setDenominacio("Concert de primavera");
        old_event.setDataInici("Dimarts");
        old_event.setUbicacio("Barcelona");
        old_event.setAdreca("C/ Quinta Forca");
        old_event.setEspai("Sideral");

        Event updatedEvent = new Event();
        updatedEvent.setId(123L);
        updatedEvent.setDenominacio("Concert de primavera");
        updatedEvent.setDataInici("Dimarts");
        updatedEvent.setUbicacio("Tarragona");
        updatedEvent.setAdreca("C/ Quinta Forca");
        updatedEvent.setEspai("Sideral");

        Event expected = new Event();
        expected.setId(123L);
        expected.setDenominacio("Concert de primavera");
        expected.setDataInici("Dimarts");
        expected.setUbicacio("Tarragona");
        expected.setAdreca("C/ Quinta Forca");
        expected.setEspai("Sideral");


        given(eventJpaRepository.save(any(Event.class))).willReturn(updatedEvent);

        given(eventJpaRepository.existsById(123L)).willReturn(true);

        Event updated = eventService.updateEvent(updatedEvent);

        Assertions.assertEquals(expected,updated);
    }




    @Test
    void processSimilarityResultOk() throws IOException {
        List<Score> remoteRequestResult = new ArrayList<>();
        remoteRequestResult.add(new Score(123L,0.8));
        remoteRequestResult.add(new Score(456L,0.2));

        Event ev1 = new Event();
        ev1.setId(123L);

        Event ev2 = new Event();
        ev2.setId(456L);

        given(similarityService.getMostSimilar("whatever")).willReturn(remoteRequestResult);
        given(eventJpaRepository.findById(123L)).willReturn(Optional.of(ev1));
        given(eventJpaRepository.findById(456L)).willReturn(Optional.of(ev2));

        List<List<Event>> aux = new LinkedList<>();
        List<Event> closeMatch = new ArrayList<>();
        closeMatch.add(ev1);
        List<Event> fuzzyMatch = new ArrayList<>();
        fuzzyMatch.add(ev2);

        aux.add(closeMatch);
        aux.add(fuzzyMatch);


        List<List<Event>> result = eventService.getBySemanticSimilarity("whatever");

        Assertions.assertEquals(aux, result);
    }

    @Test
    void processSimilarityResultVoid() throws IOException {
        List<Score> remoteRequestResult = new ArrayList<>();
        remoteRequestResult.add(new Score(123L,0.3));
        remoteRequestResult.add(new Score(456L,0.2));

        Event ev1 = new Event();
        ev1.setId(123L);

        Event ev2 = new Event();
        ev2.setId(456L);

        given(similarityService.getMostSimilar("whatever")).willReturn(remoteRequestResult);
        given(eventJpaRepository.findById(123L)).willReturn(Optional.of(ev1));
        given(eventJpaRepository.findById(456L)).willReturn(Optional.of(ev2));

        List<List<Event>> aux = new LinkedList<>();
        List<Event> closeMatch = new ArrayList<>();

        List<Event> fuzzyMatch = new ArrayList<>();
        fuzzyMatch.add(ev1);
        fuzzyMatch.add(ev2);

        aux.add(closeMatch);
        aux.add(fuzzyMatch);

        List<List<Event>> result = eventService.getBySemanticSimilarity("whatever");

        Assertions.assertEquals(aux, result);
    }

    @Test
    void cancelEventOk(){
        Event event = new Event();
        event.setId(123L);
        event.setOrganizer(new Organizer("joan"));

        given(currentUserAccessor.getCurrentUsername()).willReturn("joan");
        given(eventJpaRepository.findById(123L)).willReturn(Optional.of(event));

        eventService.cancelEvent(123L);
        Assertions.assertTrue(event.getCancelado());

    }

    @Test
    void cancelEventNotCorrectOrg(){
        Event event = new Event();
        event.setId(123L);
        event.setOrganizer(new Organizer("joaquim"));

        given(currentUserAccessor.getCurrentUsername()).willReturn("joan");
        given(eventJpaRepository.findById(123L)).willReturn(Optional.of(event));

        Assertions.assertThrows(ForbiddenActionException.class, () -> eventService.cancelEvent(123L));

    }

    @Test
    void cancelEventNoOrg(){
        Event event = new Event();
        event.setId(123L);


        given(currentUserAccessor.getCurrentUsername()).willReturn("joan");
        given(eventJpaRepository.findById(123L)).willReturn(Optional.of(event));

        Assertions.assertThrows(ForbiddenActionException.class, () -> eventService.cancelEvent(123L));

    }

}
