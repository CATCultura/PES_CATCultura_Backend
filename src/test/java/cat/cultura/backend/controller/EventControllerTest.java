package cat.cultura.backend.controller;

import cat.cultura.backend.dtos.EventDto;
import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.Organizer;
import cat.cultura.backend.exceptions.EventNotFoundException;
import cat.cultura.backend.interceptors.CurrentUserAccessor;
import cat.cultura.backend.service.EventService;
import cat.cultura.backend.service.user.UserService;
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

import java.util.*;

import org.junit.jupiter.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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

    @MockBean
    private UserService userService;

    @MockBean
    private CurrentUserAccessor currentUserAccessor;

    @Autowired
    private JacksonTester<EventDto> jsonEventDto;

    @Autowired
    private JacksonTester<Map<String,Object>> jacksonMapTester;
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
//        given(eventMapper.convertEventToDto(event)).willReturn(eventDto);
        Assertions.assertEquals(jsonEventDto.write(eventDto).getJson(), response.getContentAsString());
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
    void goesToCorrectMethodWhenQuery() throws Exception {
        // given
        Event e1 = new Event();
        Event e2 = new Event();

        List<Event> l1 = new ArrayList<>();
        l1.add(e1);
        List<Event> l2 = new ArrayList<>();
        l2.add(e2);

        Map<String,List<Event>> returnList = new HashMap<>();
        returnList.put("Similar",l1);
        returnList.put("Not similar", l2);

        given(eventService.getBySemanticSimilarity("whatever")).willReturn(returnList);

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/events?q=whatever").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());

    }

    @Test
    void goesToCorrectMethodWithoutQuery() throws Exception {
        // given
        Event e1 = new Event();


        List<Event> l1 = new ArrayList<>();
        l1.add(e1);

        Pageable pageable = PageRequest.of(0, 20);


        given(eventService.getByQuery(null,pageable)).willReturn(new PageImpl<>(l1));

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/events").accept(MediaType.APPLICATION_JSON))
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
    @WithMockUser(username = "admin", authorities = {"ORGANIZER"})
    void canCreateNewEvents() throws Exception {
        EventDto event = new EventDto();
        event.setDenominacio("titol");
        event.setDataInici("ahir");
        event.setUbicacio("BCN");
        event.setEspai("can pistraus");
        event.setAdreca("C/Pixa");
        List<EventDto> array = new ArrayList<>();
        array.add(event);

        Organizer joan = new Organizer("Joan");

        given(currentUserAccessor.getCurrentUsername()).willReturn("Joan");
        given(userService.getUserByUsername("Joan")).willReturn(joan);

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
        event.setDenominacio("titol");
//        event.setDataInici("ahir");
        event.setAdreca("C/Pixa");

        List<EventDto> array = new ArrayList<>();
        array.add(event);

        Organizer joan = new Organizer("Joan");

        given(currentUserAccessor.getCurrentUsername()).willReturn("Joan");
        given(userService.getUserByUsername("Joan")).willReturn(joan);


        // when
        MockHttpServletResponse response = mvc.perform(
                post("/events").contentType(MediaType.APPLICATION_JSON).content(
                        jsonListEventDto.write(array).getJson()
                )).andReturn().getResponse();


        // then
        Assertions.assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "ORGANIZER"})
    void canNotCreateEventWithSpecifiedId() throws Exception {
        EventDto event = new EventDto();
        event.setId(123L);
        event.setDenominacio("titol");
        event.setDataInici("ahir");


        event.setAdreca("C/Pixa");
        List<EventDto> array = new ArrayList<>();
        array.add(event);

        Organizer joan = new Organizer("Joan");

        given(currentUserAccessor.getCurrentUsername()).willReturn("Joan");
        given(userService.getUserByUsername("Joan")).willReturn(joan);
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
        Assertions.assertEquals(response.getStatus(), HttpStatus.NO_CONTENT.value());
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

    @Test
    @WithMockUser(username = "admin", authorities = { "ORGANIZER"})
    void canModifyEvent() throws Exception {
        EventDto eventDto = new EventDto();
        eventDto.setId(123L);
        eventDto.setDenominacio("titol");
        eventDto.setDataInici("ahir");
        eventDto.setAdreca("C/Pixa");
        eventDto.setUbicacio("BCN");
        eventDto.setNomOrganitzador("admin");

        Event expected = new Event();
        expected.setId(123L);
        expected.setDenominacio("titol");
        expected.setDataInici("ahir");
        expected.setAdreca("C/Pixa");
        expected.setUbicacio("BCN");
        expected.setOrganizer(new Organizer("admin"));

        given(currentUserAccessor.getCurrentUsername()).willReturn("admin");
//        given(eventMapper.convertEventDtoToEntity(eventDto)).willReturn(event);
        given(eventService.updateEvent(any(Event.class))).willReturn(expected);

        // when
        MockHttpServletResponse response = mvc.perform(
                put("/events").contentType(MediaType.APPLICATION_JSON).content(
                        jsonEventDto.write(eventDto).getJson()
                )).andReturn().getResponse();


        // then
        Assertions.assertEquals(HttpStatus.ACCEPTED.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "ORGANIZER"})
    void canNotModifyOtherEvent() throws Exception {
        EventDto eventDto = new EventDto();
        eventDto.setId(123L);
        eventDto.setDenominacio("titol");
        eventDto.setDataInici("ahir");
        eventDto.setAdreca("C/Pixa");
        eventDto.setUbicacio("BCN");
        eventDto.setNomOrganitzador("admin");

        Event expected = new Event();
        expected.setId(123L);
        expected.setDenominacio("titol");
        expected.setDataInici("ahir");
        expected.setAdreca("C/Pixa");
        expected.setUbicacio("BCN");
        expected.setOrganizer(new Organizer("admin"));

        given(currentUserAccessor.getCurrentUsername()).willReturn("josep");
//        given(eventMapper.convertEventDtoToEntity(eventDto)).willReturn(event);
        given(eventService.updateEvent(any(Event.class))).willReturn(expected);

        // when
        MockHttpServletResponse response = mvc.perform(
                put("/events").contentType(MediaType.APPLICATION_JSON).content(
                        jsonEventDto.write(eventDto).getJson()
                )).andReturn().getResponse();


        // then
        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "USER"})
    void canNotModifyEventLackOfAuth() throws Exception {
        EventDto event = new EventDto();
        event.setId(123L);
        event.setDenominacio("titol");
        event.setDataInici("ahir");
        event.setAdreca("C/Pixa");

        // when
        MockHttpServletResponse response = mvc.perform(
                put("/events").contentType(MediaType.APPLICATION_JSON).content(
                        jsonEventDto.write(event).getJson()
                )).andReturn().getResponse();


        // then
        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "ORGANIZER"})
    void canNotModifyEventWithoutId() throws Exception {
        Map<String, Object> event = new HashMap<>();
        event.put("denominacio", "titol");
        event.put("dataInici", "ahir");
        event.put("adreca", "C/Pixa");

        // when
        MockHttpServletResponse response = mvc.perform(
                put("/events").contentType(MediaType.APPLICATION_JSON).content(
                        jacksonMapTester.write(event).getJson()
                )).andReturn().getResponse();


        // then
        Assertions.assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "USER"})
    void canNotGetAttendanceCodeUSER() throws Exception {
//        Map<String, Object> event = new HashMap<>();
//        event.put("denominacio", "titol");
//        event.put("dataInici", "ahir");
//        event.put("adreca", "C/Pixa");

        // when
        MockHttpServletResponse response = mvc.perform(
                get("/events/234/attendanceCode").contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "ORGANIZER"})
    void canGetAttendanceCodeOrg() throws Exception {
//        Map<String, Object> event = new HashMap<>();
//        event.put("denominacio", "titol");
//        event.put("dataInici", "ahir");
//        event.put("adreca", "C/Pixa");

        given(eventService.getAttendanceCode(234L)).willReturn("holakase");

        // when
        MockHttpServletResponse response = mvc.perform(
                get("/events/234/attendanceCode").contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

}
