package cat.cultura.backend.service;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.repository.UserJpaRepository;
import cat.cultura.backend.service.UserService;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserJpaRepository userRepo;

    @Test
    void createUserTest() throws Exception {
        User manolo = new User("Manolo");
        manolo.setId(12L);
        manolo.setPassword("1234");
        given(userRepo.save(manolo)).willReturn(manolo);

        User client = userService.createUser(manolo);
        assertEquals(manolo.getUsername(), client.getUsername());
    }

    @Test
    void getUserByIdTest() throws Exception {
        User manolo = new User("Manolo");
        manolo.setId(2L);
        given(userRepo.findById(2L)).willReturn(Optional.of(manolo));

        User client = userService.getUserById(2L);
        assertEquals("Manolo", client.getUsername());
    }

    @Test
    void getUserByIdIfTheUserDoesNotExistTest() throws Exception {
        given(userRepo.findById(2L)).willReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                ()->userService.getUserById(2L)
        );
    }

    @Test
    void getUserByUsernameTest() throws Exception {
        User manolo = new User("Manolo");
        manolo.setId(2L);
        given(userRepo.findByUsername("Manolo")).willReturn(Optional.of(manolo));

        User client = userService.getUserByUsername("Manolo");
        assertEquals("Manolo", client.getUsername());
    }

    @Test
    void getUserByUsernameIfTheUserDoesNotExistTest() throws Exception {
        given(userRepo.findByUsername("Manolo")).willReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                ()->userService.getUserByUsername("Manolo")
        );
    }

    @Test
    void updateUserTest() throws Exception {
        User manolo = new User("Manolo");
        given(userRepo.save(manolo)).willReturn(manolo);

        User client = userService.updateUser(manolo);
        assertEquals("Manolo", client.getUsername());
    }

    @Test
    void getFavouriteEventsByIdTest() throws Exception {
        User manolo = new User("Manolo");
        manolo.setId(2L);
        Event favourite = new Event();
        favourite.setId(3L);
        List<Event> favourites = new ArrayList<>();
        favourites.add(favourite);
        manolo.setFavourites(favourites);
        given(userRepo.findById(2L)).willReturn(Optional.of(manolo));

        List<Event> clientFavourites = userService.getFavouriteEventsById(2L);
        assertEquals(manolo.getFavourites(), clientFavourites);
    }

    @Test
    void getFavouriteEventsByIdIfTheUserDoesNotExistTest() throws Exception {
        Optional<User> result = Optional.empty();
        given(userRepo.findById(2L)).willReturn(result);

        assertThrows(
                UserNotFoundException.class,
                ()->userService.getFavouriteEventsById(2L)
        );
    }

    @Test
    void getAttendanceEventsByIdTest() throws Exception {
        User manolo = new User("Manolo");
        manolo.setId(2L);
        Event attendance = new Event();
        attendance.setId(3L);
        List<Event> att = new ArrayList<>();
        att.add(attendance);
        manolo.setFavourites(att);
        given(userRepo.findById(2L)).willReturn(Optional.of(manolo));

        List<Event> clientAttendance = userService.getAttendanceEventsById(2L);
        assertEquals(manolo.getAttendance(), clientAttendance);
    }

    @Test
    void getAttendanceEventsByIdIfTheUserDoesNotExistTest() throws Exception {
        Optional<User> result = Optional.empty();
        given(userRepo.findById(2L)).willReturn(result);

        assertThrows(
                UserNotFoundException.class,
                ()->userService.getAttendanceEventsById(2L)
        );
    }

    @Test
    void getUsersByQueryIdTest() throws Exception {
        User manolo = new User("Manolo");
        manolo.setId(2L);
        List<User> users = new ArrayList<>();
        users.add(manolo);
        Page<User> page = new PageImpl<>(users);
        Pageable pageable = PageRequest.of(0, 20);
        given(userRepo.getUsersByQuery(2L, null, null, pageable)).willReturn(page);

        Page<User> result = userService.getUsersByQuery(2l,null,null,pageable);
        assertEquals(page, result);
    }

    @Test
    void getUsersByQueryUsernameTest() throws Exception {
        User manolo = new User("Manolo");
        manolo.setId(2L);
        List<User> users = new ArrayList<>();
        users.add(manolo);
        Page<User> page = new PageImpl<>(users);
        Pageable pageable = PageRequest.of(0, 20);
        given(userRepo.getUsersByQuery(null, "Manolo", null, pageable)).willReturn(page);

        Page<User> result = userService.getUsersByQuery(null,"Manolo",null,pageable);
        assertEquals(page, result);
    }

    @Test
    void getUsersByQueryNameAndSurnameTest() throws Exception {
        User manolo = new User("Manolo");
        manolo.setId(2L);
        manolo.setNameAndSurname("Manolo Eldelbombo");
        List<User> users = new ArrayList<>();
        users.add(manolo);
        Page<User> page = new PageImpl<>(users);
        Pageable pageable = PageRequest.of(0, 20);
        given(userRepo.getUsersByQuery(null, null, "Manolo Eldelbombo", pageable)).willReturn(page);

        Page<User> result = userService.getUsersByQuery(null,null,"Manolo Eldelbombo",pageable);
        assertEquals(page, result);
    }

    @Test
    void getUsersByQueryIfTheyDoNotExistTest() throws Exception {
        List<User> users = new ArrayList<>();
        Page<User> page = new PageImpl<>(users);
        Pageable pageable = PageRequest.of(0, 20);
        given(userRepo.getUsersByQuery(null, null, "Manolo Eldelbombo", pageable)).willReturn(page);
        assertThrows(
                UserNotFoundException.class,
                ()->userService.getUsersByQuery(null,null,"Manolo Eldelbombo",pageable)
        );
    }

    @Test
    void createUser() {
        User received = new User("pepitovadecurt");
        received.setPassword("1234");
        User returnedAfterSave = new User("pepitovadecurt");
        returnedAfterSave.setPassword("1234");
        returnedAfterSave.setId(45L);

        given(userRepo.save(received)).willReturn(returnedAfterSave);

        User expected = new User("pepitovadecurt");
        expected.setId(45L);
        String h = expected.createUserHash();

        User actualResult = userService.createUser(received);

        Assertions.assertEquals(actualResult.getUserHash(), h);
    }

    @Test
    void createUsers() {
        User received = new User("pepitovadecurt");
        received.setPassword("1234");

        User returnedAfterSave = new User("pepitovadecurt");
        returnedAfterSave.setPassword("1234");
        returnedAfterSave.setId(45L);

        given(userRepo.save(received)).willReturn(returnedAfterSave);

        List<User> usersReceived = new ArrayList<>();
        usersReceived.add(received);
        usersReceived.add(received);

        User expected = new User("pepitovadecurt");
        expected.setId(45L);
        String h = expected.createUserHash();

        List<User> actualResult = userService.createUsers(usersReceived);

        for (User u : actualResult) {
            Assertions.assertEquals(u.getUserHash(), h);
        }
    }

}
