package cat.cultura.backend.entity;

import cat.cultura.backend.entity.tag.Tag;
import cat.cultura.backend.entity.tag.TagAltresCategories;
import cat.cultura.backend.entity.tag.TagAmbits;
import cat.cultura.backend.entity.tag.TagCategories;
import cat.cultura.backend.exceptions.TagAlreadyAddedException;
import cat.cultura.backend.exceptions.TagNotPresentException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Test
    void hasFriendFalseTest() {
        User u1 = new User("Joan");
        User u2 = new User("Pep");

        Assertions.assertFalse(u1.hasFriend(u2));
    }

    @Test
    void hasFriendTrueTest() {
        User u1 = new User("Joan");
        User u2 = new User("Pep");

        u1.addFriend(u2);

        Assertions.assertTrue(u1.hasFriend(u2));
    }

    @Test
    void addFriendOkTest() {
        User u1 = new User("Joan");
        User u2 = new User("Pep");

        u1.addFriend(u2);

        Assertions.assertTrue(u1.hasFriend(u2) && u2.hasFriend(u1));
    }

    @Test
    void addFriendSeveralTimesTest() {
        User u1 = new User("Joan");
        User u2 = new User("Pep");

        u1.addFriend(u2);
        u1.addFriend(u2);
        u1.addFriend(u2);
        u1.addFriend(u2);

        Assertions.assertEquals(1,u1.getFriends().size());
    }

    @Test
    void addSelfFriendTest() {
        User u1 = new User("Joan");

        Assertions.assertThrows(AssertionError.class, () -> u1.addFriend(u1));
    }

    @Test
    void removeFriendTest() {
        User u1 = new User("Joan");
        User u2 = new User("Pep");

        u1.addFriend(u2);
        u1.removeFriend(u2);

        Assertions.assertFalse(u1.hasFriend(u2) && u2.hasFriend(u1));
    }

    @Test
    void removeNotFriendTest() {
        User u1 = new User("Joan");
        User u2 = new User("Pep");

        u1.removeFriend(u2);

        Assertions.assertFalse(u1.hasFriend(u2) && u2.hasFriend(u1));
    }

    @Test
    void addAmbitsTagOk() {
        User u = new User("JoanJosep");
        Tag t = new TagAmbits("arts-visuals");
        u.addTag(t);

        List<Tag> tagList = u.getTagsAmbits();
        Assertions.assertTrue(tagList.contains(t));

    }

    @Test
    void addAmbitsTagRepeated() {
        User u = new User("JoanJosep");
        Tag t = new TagAmbits("arts-visuals");
        u.addTag(t);

        Assertions.assertThrows(TagAlreadyAddedException.class, () -> u.addTag(t));
    }

    @Test
    void addCategTagOk() {
        User u = new User("JoanJosep");
        Tag t = new TagCategories("arts-visuals");
        u.addTag(t);

        List<Tag> tagList = u.getTagsCateg();
        Assertions.assertTrue(tagList.contains(t));

    }

    @Test
    void addCategTagRepeated() {
        User u = new User("JoanJosep");
        Tag t = new TagCategories("arts-visuals");
        u.addTag(t);

        Assertions.assertThrows(TagAlreadyAddedException.class, () -> u.addTag(t));
    }

    @Test
    void addAltresCategTagOk() {
        User u = new User("JoanJosep");
        Tag t = new TagAltresCategories("arts-visuals");
        u.addTag(t);

        List<Tag> tagList = u.getTagsAltresCateg();
        Assertions.assertTrue(tagList.contains(t));

    }

    @Test
    void addAltresCategTagRepeated() {
        User u = new User("JoanJosep");
        Tag t = new TagAltresCategories("arts-visuals");
        u.addTag(t);

        Assertions.assertThrows(TagAlreadyAddedException.class, () -> u.addTag(t));
    }

    //remove tags

    @Test
    void removeAmbitsTagOk() {
        User u = new User("JoanJosep");
        Tag t = new TagAmbits("arts-visuals");
        u.addTag(t);

        List<Tag> tagList = u.getTagsAmbits();
        Assertions.assertTrue(tagList.contains(t));
        u.removeTag(t);
        Assertions.assertFalse(tagList.contains(t));
    }

    @Test
    void removeTagNotPresent() {
        User u = new User("JoanJosep");
        Tag t = new TagAmbits("arts-visuals");

        Assertions.assertThrows(TagNotPresentException.class, () -> u.removeTag(t));
    }

    @Test
    void removeCategTagOk() {
        User u = new User("JoanJosep");
        Tag t = new TagCategories("arts-visuals");
        u.addTag(t);

        List<Tag> tagList = u.getTagsCateg();
        Assertions.assertTrue(tagList.contains(t));
        u.removeTag(t);
        Assertions.assertFalse(tagList.contains(t));
    }

    @Test
    void removeAltresCategTagOk() {
        User u = new User("JoanJosep");
        Tag t = new TagAltresCategories("arts-visuals");
        u.addTag(t);
        List<Tag> tagList = u.getTagsAltresCateg();
        Assertions.assertTrue(tagList.contains(t));
        u.removeTag(t);
        Assertions.assertFalse(tagList.contains(t));
    }

    @Test
    void getAllTags() {
        User u = new User("JoanJosep");
        Tag ta = new TagAmbits("arts-visuals");
        Tag tc = new TagCategories("exposicions");
        Tag tac = new TagAltresCategories("nadal");
        u.addTag(ta);
        u.addTag(tc);
        u.addTag(tac);

        Set<Tag> expected = new HashSet<>();
        expected.add(ta);
        expected.add(tc);
        expected.add(tac);

        Assertions.assertEquals(expected,u.getTags());

    }




}
