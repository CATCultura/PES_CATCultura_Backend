package cat.cultura.backend.service.user;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.Organizer;
import cat.cultura.backend.entity.Role;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.ForbiddenActionException;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.interceptors.CurrentUserAccessor;
import cat.cultura.backend.repository.UserJpaRepository;
import cat.cultura.backend.service.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private UserJpaRepository userRepo;

    @MockBean
    private CurrentUserAccessor currentUserAccessor;

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
        given(userRepo.findById(2L)).willReturn(Optional.empty());

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
    void reportErrorOneOfTheUsersDoNotExist() throws Exception {
        given(userRepo.findById(1L)).willReturn(Optional.empty());
        given(userRepo.findById(4L)).willReturn(Optional.empty());
        assertThrows(
                UserNotFoundException.class,
                ()->userService.report(1L,4L)
        );
    }

    @Test
    void reportErrorSameUserTest() throws Exception {
        assertThrows(
                AssertionError.class,
                ()->userService.report(1L,1L)
        );
    }

    @Test
    void reportTest() throws Exception {
        User user1 = new User();
        User user2 = new User();
        user1.setId(3L);
        user2.setId(4L);
        given(userRepo.findById(3L)).willReturn(Optional.of(user1));
        given(userRepo.findById(4L)).willReturn(Optional.of(user2));
        List<User> expectedReportedUsers = new ArrayList<>();
        expectedReportedUsers.add(user2);
        List<User> reportedUsers = userService.report(3L,4L);
        assertEquals(expectedReportedUsers, reportedUsers);
    }

    @Test
    void quitReportThatDoesNotExistTest() throws Exception {
        User user1 = new User();
        User user2 = new User();
        user1.setId(3L);
        user2.setId(4L);
        given(userRepo.findById(3L)).willReturn(Optional.of(user1));
        given(userRepo.findById(4L)).willReturn(Optional.of(user2));
        assertThrows(
                AssertionError.class,
                ()->userService.quitReport(3L,4L)
        );
    }

    @Test
    void quitReportTest() throws Exception {
        User user1 = new User();
        User user2 = new User();
        user1.setId(3L);
        user2.setId(4L);
        given(userRepo.findById(3L)).willReturn(Optional.of(user1));
        given(userRepo.findById(4L)).willReturn(Optional.of(user2));
        List<User> expectedReportedUsers = new ArrayList<>();
        userService.report(3L,4L);
        List<User> reportedUsers = userService.quitReport(3L,4L);
        assertEquals(expectedReportedUsers, reportedUsers);
    }

    @Test
    void getMostReportedTest() throws Exception {
        User user1 = new User();
        user1.setId(3L);
        List<User> expectedReportedUsers = new ArrayList<>();
        expectedReportedUsers.add(user1);
        given(userRepo.findReportedUsers()).willReturn(expectedReportedUsers);
        List<User> mostReportedUsers = userService.getMostReportedUsers();
        assertEquals(expectedReportedUsers, mostReportedUsers);
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
    void createOrganizer() {
        Organizer received = new Organizer("pepitovadecurt");
        received.setPassword("1234");
        Organizer returnedAfterSave = new Organizer("pepitovadecurt");
        returnedAfterSave.setPassword("1234");
        returnedAfterSave.setId(45L);
        returnedAfterSave.setRole(Role.ORGANIZER);

        given(userRepo.save(received)).willReturn(returnedAfterSave);

        User expected = new User("pepitovadecurt");
        expected.setId(45L);
        String h = expected.createUserHash();

        User actualResult = userService.createOrganizer(received);

        Assertions.assertEquals(actualResult.getUserHash(), h);
        Assertions.assertEquals(Role.ORGANIZER, actualResult.getRole());
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

    @Test
    void cannotChangeOtherUserPass() {
        User u = new User("Joan");
        u.setId(123L);
        u.setPassword("blabla");

        given(userRepo.findById(123L)).willReturn(Optional.of(u));
        given(currentUserAccessor.getCurrentUsername()).willReturn("Pere");

        Assertions.assertThrows(ForbiddenActionException.class,()->userService.changePassword(123L,"bro"));

    }

    @Test
    void canChangeUserPassOk() {
        User u = new User("Joan");
        u.setId(123L);
        u.setPassword("blabla");

        given(userRepo.findById(123L)).willReturn(Optional.of(u));
        given(currentUserAccessor.getCurrentUsername()).willReturn("Joan");


        given(passwordEncoder.encode(any())).willReturn("bro");

        userService.changePassword(123L,"bro");

        Assertions.assertEquals("bro",u.getPassword());


    }

}
