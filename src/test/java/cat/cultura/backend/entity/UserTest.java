package cat.cultura.backend.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

@ActiveProfiles("test")
class UserTest {


    @Test
    void addFavouriteTest() {
        User u = new User("Joan");
        Event ev1 = new Event();
        ev1.setDenominacio("Concert de primavera");
        ev1.setDataInici("Dimarts");
        ev1.setUbicacio("Barcelona");
        ev1.setAdreca("C/ Quinta Forca");
        ev1.setEspai("Sideral");

        List<Event> expected = new ArrayList<>();
        expected.add(ev1);
        u.addFavourite(ev1);
        Assertions.assertEquals(expected, u.getFavourites());
    }

    @Test
    void addFavouriteTestError() {
        User u = new User("Joan");
        Event ev1 = new Event();
        ev1.setDenominacio("Concert de primavera");
        ev1.setDataInici("Dimarts");
        ev1.setUbicacio("Barcelona");
        ev1.setAdreca("C/ Quinta Forca");
        ev1.setEspai("Sideral");

        u.addFavourite(ev1);
        Assertions.assertThrows(AssertionError.class, () -> u.addFavourite(ev1));
    }
    @Test
    void removeFromFavouritesTest() {
        User u = new User("Joan");
        Event ev1 = new Event();
        ev1.setDenominacio("Concert de primavera");
        ev1.setDataInici("Dimarts");
        ev1.setUbicacio("Barcelona");
        ev1.setAdreca("C/ Quinta Forca");
        ev1.setEspai("Sideral");

        Event ev2 = new Event();
        ev2.setDenominacio("Concert de primavera");
        ev2.setDataInici("Dimarts");
        ev2.setUbicacio("Barcelona");
        ev2.setAdreca("C/ Quarta Forca");
        ev2.setEspai("Sideral");

        List<Event> expected = new ArrayList<>();
        expected.add(ev1);
        u.addFavourite(ev1);
        u.addFavourite(ev2);
        u.removeFavourite(ev2);
        Assertions.assertEquals(expected, u.getFavourites());
    }

    @Test
    void removeFavouriteTestError() {
        User u = new User("Joan");
        Event ev1 = new Event();
        ev1.setDenominacio("Concert de primavera");
        ev1.setDataInici("Dimarts");
        ev1.setUbicacio("Barcelona");
        ev1.setAdreca("C/ Quinta Forca");
        ev1.setEspai("Sideral");

        Event ev2 = new Event();
        ev2.setDenominacio("Concert de primavera");
        ev2.setDataInici("Dimarts");
        ev2.setUbicacio("Barcelona");
        ev2.setAdreca("C/ Quarta Forca");
        ev2.setEspai("Sideral");

        u.addFavourite(ev1);
        Assertions.assertThrows(AssertionError.class, () -> u.removeFavourite(ev2));
    }

    @Test
    void addAttendanceTest() {
        User u = new User("Joan");
        Event ev1 = new Event();
        ev1.setDenominacio("Concert de primavera");
        ev1.setDataInici("Dimarts");
        ev1.setUbicacio("Barcelona");
        ev1.setAdreca("C/ Quinta Forca");
        ev1.setEspai("Sideral");

        List<Event> expected = new ArrayList<>();
        expected.add(ev1);

        u.addAttendance(ev1);

        Assertions.assertEquals(expected, u.getAttendance());
    }

    @Test
    void addAttendanceTestError() {
        User u = new User("Joan");
        Event ev1 = new Event();
        ev1.setDenominacio("Concert de primavera");
        ev1.setDataInici("Dimarts");
        ev1.setUbicacio("Barcelona");
        ev1.setAdreca("C/ Quinta Forca");
        ev1.setEspai("Sideral");
        u.addAttendance(ev1);
        Assertions.assertThrows(AssertionError.class, () -> u.addAttendance(ev1));
    }
    @Test
    void removeFromAttendanceTest() {
        User u = new User("Joan");
        Event ev1 = new Event();
        ev1.setDenominacio("Concert de primavera");
        ev1.setDataInici("Dimarts");
        ev1.setUbicacio("Barcelona");
        ev1.setAdreca("C/ Quinta Forca");
        ev1.setEspai("Sideral");

        Event ev2 = new Event();
        ev2.setDenominacio("Concert de primavera");
        ev2.setDataInici("Dimarts");
        ev2.setUbicacio("Barcelona");
        ev2.setAdreca("C/ Quarta Forca");
        ev2.setEspai("Sideral");
        List<Event> expected = new ArrayList<>();
        expected.add(ev1);
        u.addAttendance(ev1);
        u.addAttendance(ev2);
        u.removeAttendance(ev2);
        Assertions.assertEquals(expected, u.getAttendance());
    }

    @Test
    void removeAttendanceTestError() {
        User u = new User("Joan");
        Event ev1 = new Event();
        ev1.setDenominacio("Concert de primavera");
        ev1.setDataInici("Dimarts");
        ev1.setUbicacio("Barcelona");
        ev1.setAdreca("C/ Quinta Forca");
        ev1.setEspai("Sideral");

        Event ev2 = new Event();
        ev2.setDenominacio("Concert de primavera");
        ev2.setDataInici("Dimarts");
        ev2.setUbicacio("Barcelona");
        ev2.setAdreca("C/ Quarta Forca");
        ev2.setEspai("Sideral");
        u.addAttendance(ev1);
        Assertions.assertThrows(AssertionError.class, () -> u.removeAttendance(ev2));
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
        u.addTrophy(t1);
        u.addTrophy(t2);
        u.removeTrophy(t2);
        Assertions.assertThrows(AssertionError.class, () -> u.removeTrophy(t2));
    }


}
