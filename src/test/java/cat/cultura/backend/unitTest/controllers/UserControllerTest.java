package cat.cultura.backend.unitTest.controllers;

import cat.cultura.backend.dtos.UserDto;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.service.FavouriteService;
import cat.cultura.backend.service.RequestService;
import cat.cultura.backend.service.UserService;
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

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
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
    public void canRetrieveUser() throws Exception {
        // given
        User user = new User();
        user.setId(2L);
        given(userService.getUserById(2L)).willReturn(user);

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/users/2").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertEquals(response.getStatus(),HttpStatus.OK.value());
        UserDto userDto = new UserDto();
        userDto.setId(2L);
        assertEquals(response.getContentAsString(),jsonUserDto.write(userDto).getJson());
    }
}
