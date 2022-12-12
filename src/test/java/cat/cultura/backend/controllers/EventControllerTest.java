package cat.cultura.backend.controllers;

import cat.cultura.backend.dtos.EventDto;
import cat.cultura.backend.entity.Event;
import cat.cultura.backend.exceptions.EventNotFoundException;
import cat.cultura.backend.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
class EventControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private EventService eventService;

    @Autowired
    private JacksonTester<EventDto> jsonEventDto;

    @Autowired
    private JacksonTester<List<EventDto>> jsonListEventDto;

    @Test
    @WithMockUser(username = "admin", authorities = { "USER"})
    void canRetrieveByIdWhenExists() throws Exception {
        // given
        Event event = new Event();
        event.setId(2L);
        given(eventService.getEventById(2L)).willReturn(event);

        // when
        MockHttpServletResponse response = mvc.perform(
                get("/events/2").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        Assertions.assertEquals(response.getStatus(), HttpStatus.OK.value());
        EventDto eventDto = new EventDto();
        eventDto.setId(2L);
        Assertions.assertEquals(response.getContentAsString(), jsonEventDto.write(eventDto).getJson());
    }

    @Test
    void canRetrieveEventsAnonymous() throws Exception {
        // given
        Event event = new Event();
        event.setId(8L);
        event.setDenominacio("titol");
        event.setDataInici("ahir");
        event.setUbicacio("BCN");
        event.setEspai("can pistraus");
        event.setAdreca("C/Pixa");
        List<Event> eventList = new ArrayList<>();
        eventList.add(event);
        Page<Event> page = new PageImpl<>(eventList);
        Pageable pageable = PageRequest.of(0, 20);
        given(eventService.getByQuery(null,pageable)).willReturn(page);

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/events").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        Assertions.assertEquals(response.getStatus(), HttpStatus.OK.value());
    }

    @Test
    void canRetrieveSingleEventAnonymous() throws Exception {
        // given
        Event event = new Event();
        event.setId(8L);
        event.setDenominacio("titol");
        event.setDataInici("ahir");
        event.setUbicacio("BCN");
        event.setEspai("can pistraus");
        event.setAdreca("C/Pixa");
        given(eventService.getEventById(8L)).willReturn(event);

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/events/8").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "USER"})
    void canRetrieveByIdWhenDoesNotExist() throws Exception {
        // given
        given(eventService.getEventById(2L)).willThrow(new EventNotFoundException());

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/events/2").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        Assertions.assertEquals(response.getStatus(), HttpStatus.NOT_FOUND.value());
//        assertThat(response.getContentAsString().isEmpty());
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "ORGANIZER"})
    void canCreateNewEvents() throws Exception {
        EventDto event = new EventDto();
        event.setId(8L);
        event.setDenominacio("titol");
        event.setDataInici("ahir");
        event.setUbicacio("BCN");
        event.setEspai("can pistraus");
        event.setAdreca("C/Pixa");
        List<EventDto> array = new ArrayList<>();
        array.add(event);

        // when
        MockHttpServletResponse response = mvc.perform(
                post("/events").contentType(MediaType.APPLICATION_JSON).content(
                        jsonListEventDto.write(array).getJson()
                )).andReturn().getResponse();


        // then
        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "ORGANIZER"})
    void canNotCreateNewEventsWihtMissinParams() throws Exception {
        EventDto event = new EventDto();
        event.setId(8L);
        event.setDenominacio("titol");
        event.setDataInici("ahir");


        event.setAdreca("C/Pixa");
        List<EventDto> array = new ArrayList<>();
        array.add(event);

        // when
        MockHttpServletResponse response = mvc.perform(
                post("/events").contentType(MediaType.APPLICATION_JSON).content(
                        jsonListEventDto.write(array).getJson()
                )).andReturn().getResponse();


        // then
        Assertions.assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "USER"})
    void canNotCreateNewEvents() throws Exception {
        EventDto event = new EventDto();
        event.setId(8L);
        event.setDenominacio("titol");
        event.setDataInici("ahir");
        event.setUbicacio("BCN");
        event.setEspai("can pistraus");
        event.setAdreca("C/Pixa");
        List<EventDto> array = new ArrayList<>();
        array.add(event);

        // when
        MockHttpServletResponse response = mvc.perform(
                post("/events").contentType(MediaType.APPLICATION_JSON).content(
                        jsonListEventDto.write(array).getJson()
                )).andReturn().getResponse();


        // then
        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "ORGANIZER"})
    void canDeleteEvent() throws Exception {
        // when
        MockHttpServletResponse response = mvc.perform(
                delete("/events/2").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION,"somthg")).andReturn().getResponse();

        // then
        Assertions.assertEquals(response.getStatus(), HttpStatus.OK.value());
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "USER"})
    void cannotDeleteEventUser() throws Exception {
        // when
        MockHttpServletResponse response = mvc.perform(
                delete("/events/2").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION,"somthg")).andReturn().getResponse();

        // then
        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "ORGANIZER"})
    void canDeleteEventWhenTheyDoNotExist() throws Exception {
        // given
        doThrow(new EventNotFoundException()).when(eventService).deleteEvent(800L);

        // when
        MockHttpServletResponse response = mvc.perform(
                delete("/events/800").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION,"somthg")).andReturn().getResponse();

        // then
        Assertions.assertEquals(response.getStatus(), HttpStatus.NOT_FOUND.value());
    }

}
