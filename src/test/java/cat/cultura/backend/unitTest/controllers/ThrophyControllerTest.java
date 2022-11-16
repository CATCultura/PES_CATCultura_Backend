package cat.cultura.backend.unitTest.controllers;

import cat.cultura.backend.dtos.TrophyDto;
import cat.cultura.backend.entity.Trophy;
import cat.cultura.backend.service.TrophyService;
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

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
public class ThrophyControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TrophyService trophyService;

    @Autowired
    private JacksonTester<TrophyDto> jsonTrophyDto;

    @Autowired
    private JacksonTester<List<TrophyDto>> jsonListTrophyDto;

//    @Test
//    public void canCreateATrophy() throws Exception {
//        // given
//        TrophyDto trophyDto = new TrophyDto();
//        trophyDto.setId(2L);
//
//        // when
//        MockHttpServletResponse response = mvc.perform(
//                post("/trophies").contentType(MediaType.APPLICATION_JSON).content(
//                        jsonTrophyDto.write(trophyDto).getJson()
//                )).andReturn().getResponse();
//
//        // then
//        assertEquals(response.getStatus(),HttpStatus.CREATED.value());
//    }

    @Test
    public void canRetrieveTrophies() throws Exception {
        // given
        Trophy trophy = new Trophy();
        trophy.setId(2L);
        List<Trophy> array = new ArrayList<>();
        array.add(trophy);
        given(trophyService.getTrophies()).willReturn(array);

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/trophies").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertEquals(response.getStatus(),HttpStatus.OK.value());
        TrophyDto trophyDto = new TrophyDto();
        trophyDto.setId(2L);
        List<TrophyDto> array2 = new ArrayList<>();
        array2.add(trophyDto);
        assertEquals(response.getContentAsString(),jsonListTrophyDto.write(array2).getJson());
    }

//    @Test
//    public void canRetrieveTrophiesWhenTheyDoNotExists() throws Exception {
//        // given
//        given(trophyService.getTrophies()).willThrow(new TrophyNotFoundException());
//
//        // when
//        MockHttpServletResponse response = mvc.perform(
//                        get("/trophies").accept(MediaType.APPLICATION_JSON))
//                .andReturn().getResponse();
//
//        // then
//        assertEquals(response.getStatus(),HttpStatus.NOT_FOUND.value());
//        assertThat(response.getContentAsString().isEmpty());
//    }

    @Test
    public void canDeleteTrophy() throws Exception {
        // when
        MockHttpServletResponse response = mvc.perform(
                delete("/trophies/2").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        assertEquals(response.getStatus(),HttpStatus.OK.value());
    }

//    @Test
//    public void canDeleteEventWhenTheyDoNotExist() throws Exception {
//        // given
//        doThrow(new TrophyNotFoundException()).when(trophyService).deleteTrophy(800L);
//
//        // when
//        MockHttpServletResponse response = mvc.perform(
//                delete("/trophies/800").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
//
//        // then
//        assertEquals(response.getStatus(),HttpStatus.NOT_FOUND.value());
//    }
}
