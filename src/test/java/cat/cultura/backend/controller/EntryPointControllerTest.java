package cat.cultura.backend.controller;

import cat.cultura.backend.dtos.EventDto;
import cat.cultura.backend.entity.Event;
import cat.cultura.backend.service.EventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
class EntryPointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Autowired
    private JacksonTester<EventDto> jsonEventDto;

    @Autowired
    private JacksonTester<List<EventDto>> jsonListEventDto;

    @Test
    @WithMockUser(username = "admin", authorities = { "USER"})
    void updateDBShouldFailForUser() throws Exception {
        EventDto event = new EventDto();
        event.setDenominacio("titol");
        event.setDataInici("ahir");
        event.setUbicacio("BCN");
        event.setEspai("can pistraus");
        event.setAdreca("C/Pixa");
        List<EventDto> array = new ArrayList<>();
        array.add(event);
        MockHttpServletResponse response = mockMvc.perform(
                post("/insert").contentType(MediaType.APPLICATION_JSON).header("auth-token", "auth-token").content(
                        jsonListEventDto.write(array).getJson()
                )).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "SERVICE"})
    void updateDBShouldBeOkForService() throws Exception {
        EventDto event = new EventDto();
        event.setDenominacio("titol");
        event.setDataInici("ahir");
        event.setUbicacio("BCN");
        event.setEspai("can pistraus");
        event.setAdreca("C/Pixa");
        List<EventDto> array = new ArrayList<>();
        array.add(event);
        MockHttpServletResponse response = mockMvc.perform(
                post("/insert").contentType(MediaType.APPLICATION_JSON).content(
                        jsonListEventDto.write(array).getJson()
                )).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }



    @Test
    @WithMockUser(username = "admin", authorities = { "USER"})
    void getAllEventsShouldFailForUser() throws Exception {
        Event event = new Event();
        event.setId(2L);
        given(eventService.getEventById(2L)).willReturn(event);

        MockHttpServletResponse response = mockMvc.perform(
                        get("/allevents").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "SERVICE"})
    void getAllEventsShouldBeOkForService() throws Exception {

        Event event = new Event();
        event.setId(2L);
        List<Event> events = new ArrayList<>();
        events.add(event);
        given(eventService.getEvents()).willReturn(events);

        MockHttpServletResponse response = mockMvc.perform(
                        get("/allevents").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
}