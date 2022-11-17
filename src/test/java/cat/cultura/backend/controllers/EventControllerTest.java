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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
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
    void canRetrieveByIdWhenDoesNotExist() throws Exception {
        // given
        given(eventService.getEventById(2L)).willThrow(new EventNotFoundException());

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/events/2").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        Assertions.assertEquals(response.getStatus(), HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString().isEmpty());
    }

//    @Test
//    public void canCreateNewEvents() throws Exception {
//        EventDto event = new EventDto();
//        event.setId(8L);
//        List<EventDto> array = new ArrayList<>();
//        array.add(event);
//
//        // when
//        MockHttpServletResponse response = mvc.perform(
//                post("/events").contentType(MediaType.APPLICATION_JSON).content(
//                        jsonListEventDto.write(array).getJson()
//                )).andReturn().getResponse();
//
//
//        // then
//        Assertions.assertEquals(response.getStatus(), HttpStatus.CREATED.value());
//    }

    @Test
    void canDeleteEvent() throws Exception {
        // when
        MockHttpServletResponse response = mvc.perform(
                delete("/events/2").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        Assertions.assertEquals(response.getStatus(), HttpStatus.OK.value());
    }

    @Test
    void canDeleteEventWhenTheyDoNotExist() throws Exception {
        // given
        doThrow(new EventNotFoundException()).when(eventService).deleteEvent(800L);

        // when
        MockHttpServletResponse response = mvc.perform(
                delete("/events/800").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        Assertions.assertEquals(response.getStatus(), HttpStatus.NOT_FOUND.value());
    }

}
