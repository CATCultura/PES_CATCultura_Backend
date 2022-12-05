package cat.cultura.backend.controller;

import cat.cultura.backend.dtos.EventDto;
import cat.cultura.backend.entity.Event;
import cat.cultura.backend.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Autowired
    private JacksonTester<Map<String, Set<String>>> jsonMap;

    @Autowired
    private JacksonTester<List<EventDto>> jsonListEventDto;


    @Test
    void getAllTagsWhenAnonymous() throws Exception {
        Event event = new Event();
        event.setDenominacio("titol");
        event.setDataInici("ahir");
        event.setUbicacio("BCN");
        event.setEspai("can pistraus");
        event.setAdreca("C/Pixa");
        List<String> tagsAmbits = new ArrayList<>();
        tagsAmbits.add("concert");
        event.setTagsAmbits(tagsAmbits);

        List<String> tagsCateg = new ArrayList<>();
        tagsCateg.add("nadal");

        event.setTagsCateg(tagsCateg);

        List<String> tagsAltresCateg = new ArrayList<>();
        tagsAltresCateg.add("pastorets");

        event.setTagsAltresCateg(tagsAltresCateg);


        List<Event> eventList = new ArrayList<>();
        eventList.add(event);
        given(eventService.getEvents()).willReturn(eventList);
        Map<String, Set<String>> expected = new LinkedHashMap<>();
        Set<String> ambs = new HashSet<>();
        ambs.add("concert");
        Set<String> categs = new HashSet<>();
        categs.add("nadal");
        Set<String> altresCategs = new HashSet<>();
        altresCategs.add("pastorets");
        expected.put("ambits", ambs);
        expected.put("altresCategories", altresCategs);
        expected.put("categories", categs);
        MockHttpServletResponse response = mockMvc.perform(
                get("/tags").contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        ObjectMapper objectMapper = new ObjectMapper();

        Map result = objectMapper.readValue(response.getContentAsString(), Map.class);

        Assertions.assertEquals(expected.toString(),result.toString());
    }
}