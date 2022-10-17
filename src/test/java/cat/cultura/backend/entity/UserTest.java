package cat.cultura.backend.entity;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class UserTest {


    @Test
    void addFavouriteTest() {
        User u = new User("Joan");
        Event e = new Event();
        e.setCodi(123456L);
        List<Event> expected = new ArrayList<>();
        expected.add(e);
        u.addFavourite(e);
        Assertions.assertEquals(expected, u.getFavourites());
    }

    @Test
    void addFavouriteTestError() {
        User u = new User("Joan");
        Event e = new Event();
        e.setCodi(123456L);
        u.addFavourite(e);
        Assertions.assertThrows(AssertionError.class, () -> u.addFavourite(e));
    }
    @Test
    void removeFromFavouritesTest() {
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
    void removeFavouriteTestError() {
        User u = new User("Joan");
        Event e1 = new Event();
        Event e2 = new Event();
        e1.setCodi(123456L);
        e2.setCodi(654321L);
        u.addFavourite(e1);
        Assertions.assertThrows(AssertionError.class, () -> u.removeFavourite(e2));
    }

    @Test
    void addTrophiesTest() {
        User u = new User("Joan");
        Trophy t = new Trophy();
        t.setName("TopTop");
        List<Trophy> expected = new ArrayList<>();
        expected.add(t);
        u.addTrophy(t);
        Assertions.assertEquals(expected, u.getTrophies());
    }

    @Test
    void addTrophiesErrorTest() {
        User u = new User("Joan");
        Trophy t = new Trophy();
        t.setName("TopTop");
        List<Trophy> expected = new ArrayList<>();
        expected.add(t);
        u.addTrophy(t);
        Assertions.assertThrows(AssertionError.class, () -> u.addTrophy(t));
    }

    @Test
    void removeTrophiesTest() {
        User u = new User("Joan");
        Trophy t1 = new Trophy();
        Trophy t2 = new Trophy();
        t1.setName("TopTop");
        t2.setName("Horrible");
        List<Trophy> expected = new ArrayList<>();
        expected.add(t1);
        u.addTrophy(t1);
        u.addTrophy(t2);
        u.removeTrophy(t2);
        Assertions.assertEquals(expected, u.getTrophies());
    }

    @Test
    void removeTrophiesErrorTest() {
        User u = new User("Joan");
        Trophy t1 = new Trophy();
        Trophy t2 = new Trophy();
        t1.setName("TopTop");
        t2.setName("Horrible");
        List<Trophy> expected = new ArrayList<>();
        expected.add(t1);
        u.addTrophy(t1);
        u.addTrophy(t2);
        u.removeTrophy(t2);
        Assertions.assertThrows(AssertionError.class, () -> u.removeTrophy(t2));
    }


}
