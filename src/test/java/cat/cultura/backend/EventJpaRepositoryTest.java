package cat.cultura.backend;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.repository.EventJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EventJpaRepositoryTest {
    @Autowired
    private EventJpaRepository repo;

    @Test
    public void saveEvent() {
        Event e1 = new Event();
        e1.setCodi(123456789);
        e1.setDataFi("12/01/21");

        Event e2 = new Event();
        e2.setCodi(987456123);
        e2.setDataFi("17/02/21");

        repo.save(e1);
        repo.save(e2);

        repo.flush();

        assertEquals(2, repo.findAll().size());
    }
}
