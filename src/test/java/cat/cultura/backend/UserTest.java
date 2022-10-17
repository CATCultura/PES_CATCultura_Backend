package cat.cultura.backend;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class UserTest {


    @Test
    public void addFavouriteTest() {
        User u = new User("Joan");
        Event e = new Event();
        e.setCodi(123456L);
        List<Event> expected = new ArrayList<>();
        expected.add(e);
        u.addFavourite(e);
        Assertions.assertEquals(expected, u.getFavourites());
    }

    @Test
    public void addFavouriteTestError() {
        User u = new User("Joan");
        Event e = new Event();
        e.setCodi(123456L);
        u.addFavourite(e);
        Assertions.assertThrows(AssertionError.class, () -> u.addFavourite(e));
    }
    @Test
    public void removeFromFavouritesTest() {
        User u = new User("Joan");
        Event e1 = new Event();
        Event e2 = new Event();
        e1.setCodi(123456L);
        e2.setCodi(654321L);
        List<Event> expected = new ArrayList<>();
        expected.add(e1);
        u.addFavourite(e1);
        u.addFavourite(e2);
        u.removeFavourite(e2);
        Assertions.assertEquals(expected, u.getFavourites());
    }

    @Test
    public void removeFavouriteTestError() {
        User u = new User("Joan");
        Event e1 = new Event();
        Event e2 = new Event();
        e1.setCodi(123456L);
        e2.setCodi(654321L);
        u.addFavourite(e1);
        Assertions.assertThrows(AssertionError.class, () -> u.removeFavourite(e2));
    }
}
