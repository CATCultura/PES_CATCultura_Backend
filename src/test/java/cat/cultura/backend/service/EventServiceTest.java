package cat.cultura.backend.service;


import cat.cultura.backend.entity.Event;
import cat.cultura.backend.exceptions.EventAlreadyCreatedException;
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

import static org.mockito.BDDMockito.given;

@SpringBootTest
class EventServiceTest {

    @Autowired
    EventService eventService;

    @MockBean
    EventJpaRepository eventJpaRepository;

    @MockBean
    SimilarityServiceImpl similarityService;


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

        Map<String,Object> ev2 = new HashMap<>();
        ev2.put("id",123L);
        ev2.put("denominacio", "Concert de primavera");
        ev2.put("dataInici", "Dimarts");
        ev2.put("ubicacio", "Tarragona");
        ev2.put("adreca", "C/ Quinta Forca");
        ev2.put("espai","Sideral");


        Event expected = new Event();
        expected.setId(123L);
        expected.setDenominacio("Concert de primavera");
        expected.setDataInici("Dimarts");
        expected.setUbicacio("Tarragona");
        expected.setAdreca("C/ Quinta Forca");
        expected.setEspai("Sideral");


        given(eventJpaRepository.save(old_event)).willReturn(old_event);

        given(eventJpaRepository.findById(123L)).willReturn(Optional.of(old_event));

        Event updated = eventService.updateEvent(ev2);

        Assertions.assertEquals(expected,updated);
    }

    @Test
    void testUpdateEventFewParams() {
        Event oldEvent = new Event();
        oldEvent.setId(123L);
        oldEvent.setDenominacio("Concert de primavera");
        oldEvent.setDataInici("Dimarts");
        oldEvent.setUbicacio("Barcelona");
        oldEvent.setAdreca("C/ Quinta Forca");
        oldEvent.setEspai("Sideral");

        Map<String,Object> ev2 = new HashMap<>();
        ev2.put("id",123L);
        ev2.put("ubicacio", "Tarragona");
        ev2.put("adreca", "C/ Quarta Forca");

        Event result = new Event();
        result.setId(123L);
        result.setDenominacio("Concert de primavera");
        result.setDataInici("Dimarts");
        result.setUbicacio("Tarragona");
        result.setAdreca("C/ Quarta Forca");
        result.setEspai("Sideral");


        given(eventJpaRepository.save(oldEvent)).willReturn(result);

        given(eventJpaRepository.findById(123L)).willReturn(Optional.of(oldEvent));

        Event updated = eventService.updateEvent(ev2);

        Assertions.assertEquals(result,updated);
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

        List<Event> aux = new ArrayList<>();
        aux.add(ev1);


        Page<Event> result = eventService.getBySemanticSimilarity("whatever");

        Assertions.assertEquals(new PageImpl<>(aux), result);
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

        List<Event> aux = new ArrayList<>();

        Page<Event> result = eventService.getBySemanticSimilarity("whatever");

        Assertions.assertEquals(new PageImpl<>(aux), result);
    }

}
