package cat.cultura.backend.controller;

import cat.cultura.backend.dtos.UserDto;
import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.ForbiddenActionException;
import cat.cultura.backend.service.EventService;
import cat.cultura.backend.service.user.FavouriteService;
import cat.cultura.backend.service.user.RequestService;
import cat.cultura.backend.service.user.UserService;
import cat.cultura.backend.service.user.UserTagService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;


import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private FavouriteService favouriteService;


    @MockBean
    private RequestService requestService;

    @MockBean
    private UserTagService userTagService;
    @Autowired
    private JacksonTester<UserDto> jsonUserDto;

    @Autowired
    private JacksonTester<Map<String,String>> mapJacksonTester;

    @Autowired
    private JacksonTester<List<UserDto>> jsonListUserDto;



    @Test
    @WithMockUser(username = "admin", authorities = { "USER"})
    void canRetrieveUserWhenLoggedIn() throws Exception {
        // given
        User user = new User();
        user.setId(2L);
        given(userService.getUserById(2L)).willReturn(user);

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/users/2").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION,"somthg"))
                .andReturn().getResponse();

        // then
        Assertions.assertEquals(response.getStatus(), HttpStatus.OK.value());
        UserDto userDto = new UserDto();
        userDto.setId(2L);
        Assertions.assertEquals(response.getContentAsString(), jsonUserDto.write(userDto).getJson());
    }

    @Test
    void canNotRetrieveSingleUserWhenAnonymous() throws Exception {
        // given
        User user = new User();
        user.setId(2L);
        given(userService.getUserById(2L)).willReturn(user);

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/users/2").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION,"somthg"))
                .andReturn().getResponse();

        // then
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(),response.getStatus());

    }

    @Test
    void canNotRetrieveUsersWhenAnonymous() throws Exception {
        // given
        User user = new User();
        user.setId(2L);
        List<User> userList = new ArrayList<>();
        Page<User> page = new PageImpl<>(userList);
        Pageable pageable = PageRequest.of(0, 20);
        given(userService.getUsersByQuery(null,null,null,pageable)).willReturn(page);
        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/users").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION,"somthg"))
                .andReturn().getResponse();

        // then
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(),response.getStatus());
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "USER"})
    void canRetrieveUsersWhenLoggedIn() throws Exception {
        // given
        User user = new User();
        user.setId(2L);
        List<User> userList = new ArrayList<>();
        Page<User> page = new PageImpl<>(userList);
        Pageable pageable = PageRequest.of(0, 20);
        given(userService.getUsersByQuery(null,null,null,pageable)).willReturn(page);
        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/users").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION,"somthg"))
                .andReturn().getResponse();

        // then
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
    }

    @Test
    void canRetrieveEventsOfOrgAnonymous() throws Exception {
        // given
        Event event = new Event();
        event.setId(2L);
        List<Event> eventList = new ArrayList<>();

        Pageable pageable = PageRequest.of(0, 20);
        given(userService.getOrganizedEvents(123L)).willReturn(eventList);
        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/users/123/events").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
    }

    @Test
    @WithMockUser(username = "joan", authorities = { "USER"})
    void canChangePasswordSameUser() throws Exception {
        // given
        Map<String,String> pass = new HashMap<>();
        pass.put("new_password","1234");

        // when
        MockHttpServletResponse response = mvc.perform(
                        put("/users/123/password").contentType(MediaType.APPLICATION_JSON).content(
                                mapJacksonTester.write(pass).getJson()
                        )).andReturn().getResponse();

        // then
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
    }

    @Test
    @WithMockUser(username = "joan", authorities = { "USER"})
    void cannotChangePasswordMissingParams() throws Exception {
        // given
        Map<String,String> pass = new HashMap<>();
//        pass.put("new_password","1234");

        // when
        MockHttpServletResponse response = mvc.perform(
                put("/users/123/password").contentType(MediaType.APPLICATION_JSON).content(
                        mapJacksonTester.write(pass).getJson()
                )).andReturn().getResponse();


        // then
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatus());
    }

    @Test
    @WithMockUser(username = "joan", authorities = { "USER"})
    void cannotChangePasswordWrongParams() throws Exception {
        // given
        Map<String,String> pass = new HashMap<>();
        pass.put("bla","1234");

        // when
        MockHttpServletResponse response = mvc.perform(
                put("/users/123/password").contentType(MediaType.APPLICATION_JSON).content(
                        mapJacksonTester.write(pass).getJson()
                )).andReturn().getResponse();


        // then
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatus());
    }

//    @Test
//    //@WithMockUser(username = "admin", authorities = { "USER"})
//    void canCreateUser() throws Exception {
//        // given
//        UserDto user = new UserDto();
//        user.setUsername("JosepJoan");
//        user.setNameAndSurname("Josep Sanchís");
//        user.setPassword("1234");
//        Set<String> tags = new HashSet<>();
//        tags.add("nadal");
//        tags.add("espectacles");
//        user.setTags(tags);
//
//        User createdUser = new User("JosepJoan");
//        createdUser.setId(123L);
//        createdUser.setNameAndSurname("Josep Sanchís");
//        createdUser.setPassword("1234");
//
//        given(userService.createUser(any(User.class))).willReturn(createdUser);
//
//        // when
//        MockHttpServletResponse response = mvc.perform(
//                post("/users").contentType(MediaType.APPLICATION_JSON).content(
//                        jsonUserDto.write(user).getJson()
//                )).andReturn().getResponse();
//
//        // then
//        Assertions.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
//    }
}
