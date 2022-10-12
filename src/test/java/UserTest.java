import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class UserTest {


    @Test
    public void addFavouriteTest() {
        User u = new User();
        Event e = new Event();
        e.setCodi(123456L);
        List<Event> expected = new ArrayList<>();
        expected.add(e);
        u.addFavourite(e);
        Assertions.assertEquals(expected, u.getFavourites());

    }

    @Test
    public void addFavouriteTestError() {
        User u = new User();
        Event e = new Event();
        e.setCodi(123456L);
        List<Event> expected = new ArrayList<>();
        expected.add(e);
        u.addFavourite(e);

        Assertions.assertThrows(AssertionError.class, () -> u.addFavourite(e));

    }
}
