package cat.cultura.backend.controller;

import cat.cultura.backend.dtos.EventDto;
import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.tag.Tag;
import cat.cultura.backend.entity.tag.TagAltresCategories;
import cat.cultura.backend.entity.tag.TagAmbits;
import cat.cultura.backend.entity.tag.TagCategories;
import cat.cultura.backend.service.EventService;
import cat.cultura.backend.service.TagService;
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
    private TagService tagService;

    @Test
    void getAllTagsWhenAnonymous() throws Exception {

        List<Tag> returnedTags = new ArrayList<>();
        returnedTags.add(new TagAmbits("concerts"));
        returnedTags.add(new TagCategories("nadal"));
        returnedTags.add(new TagAltresCategories("pastorets"));

        given(tagService.getAllTags()).willReturn(returnedTags);

        Map<String, Set<String>> expected = new LinkedHashMap<>();
        Set<String> ambs = new HashSet<>();
        ambs.add("concerts");
        Set<String> categs = new HashSet<>();
        categs.add("nadal");
        Set<String> altresCategs = new HashSet<>();
        altresCategs.add("pastorets");
        expected.put("AMBITS", ambs);
        expected.put("ALTRES_CATEGORIES", altresCategs);
        expected.put("CATEGORIES", categs);
        MockHttpServletResponse response = mockMvc.perform(
                get("/tags").contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        ObjectMapper objectMapper = new ObjectMapper();

        Map result = objectMapper.readValue(response.getContentAsString(), Map.class);

        Assertions.assertEquals(expected.toString(),result.toString());
    }
}