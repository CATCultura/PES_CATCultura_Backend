package cat.cultura.backend.controllers;

import cat.cultura.backend.dtos.UserDto;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.service.user.FavouriteService;
import cat.cultura.backend.service.user.RequestService;
import cat.cultura.backend.service.user.UserService;
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


import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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

    @Autowired
    private JacksonTester<UserDto> jsonUserDto;

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
}
