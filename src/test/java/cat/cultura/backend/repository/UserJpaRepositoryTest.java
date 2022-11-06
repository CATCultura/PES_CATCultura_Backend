package cat.cultura.backend.repository;

import cat.cultura.backend.BackendApplication;
import cat.cultura.backend.entity.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Stream;


@DataJpaTest
@Import(BackendApplication.class)
@ActiveProfiles("test")
public class UserJpaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    public void whenFindByName_thenReturnEmployee() {
        // given
        User alex = new User("alex");
        entityManager.persist(alex);
        entityManager.flush();

        // when
        Page<User> found = userJpaRepository.getUsersByQuery(alex.getId(), null, null, null);
        List<User> usersFound = found.get().toList();
        User userFound = usersFound.get(0);

        // then
        assertEquals(userFound.getUsername(),alex.getUsername());
    }
}
