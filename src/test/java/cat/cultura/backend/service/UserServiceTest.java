package cat.cultura.backend.service;

import cat.cultura.backend.entity.User;
import cat.cultura.backend.repository.EventJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    UserJpaRepository userJpaRepository;

    @Test
    void createUser() {
        User received = new User("pepitovadecurt");

        User returnedAfterSave = new User("pepitovadecurt");
        returnedAfterSave.setId(45L);

        given(userJpaRepository.save(received)).willReturn(returnedAfterSave);

        User expected = new User("pepitovadecurt");
        expected.setId(45L);
        int h = expected.createUserHash();

        User actualResult = userService.createUser(received);

        Assertions.assertEquals(actualResult.getUserHash(), h);
    }

    @Test
    void createUsers() {
        User received = new User("pepitovadecurt");

        User returnedAfterSave = new User("pepitovadecurt");
        returnedAfterSave.setId(45L);

        given(userJpaRepository.save(received)).willReturn(returnedAfterSave);

        List<User> usersReceived = new ArrayList<>();
        usersReceived.add(received);
        usersReceived.add(received);

        User expected = new User("pepitovadecurt");
        expected.setId(45L);
        int h = expected.createUserHash();

        List<User> actualResult = userService.createUsers(usersReceived);

        for (User u : actualResult) {
            Assertions.assertEquals(u.getUserHash(), h);
        }


    }
}