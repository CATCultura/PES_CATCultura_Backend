package cat.cultura.backend.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

@ActiveProfiles("test")
class EventTest {

    @Test
    void testEqualsTrue() {
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
        ev2.setAdreca("C/ Quinta Forca");
        ev2.setEspai("Sideral");

        Assertions.assertEquals(ev1, ev2);
    }

    @Test
    void testEqualsFalse() {
        Event ev1 = new Event();
        ev1.setDenominacio("Concert de primavera");
        ev1.setDataInici("Dimarts");
        ev1.setUbicacio("Barcelona");
        ev1.setAdreca("C/ Quinta Forca");
        ev1.setEspai("Sideral");

        Event ev2 = new Event();
        ev2.setDenominacio("Concert de tardor");
        ev2.setDataInici("Dimarts");
        ev2.setUbicacio("Barcelona");
        ev2.setAdreca("C/ Quinta Forca");
        ev2.setEspai("Sideral");

        Assertions.assertNotEquals(ev1, ev2);
    }

    @Test
    void testEqualsVoidValues() {
        Event ev1 = new Event();
        ev1.setDenominacio("Concert de primavera");
        ev1.setDataInici("Dimarts");
        ev1.setUbicacio("Barcelona");


        Event ev2 = new Event();
        ev2.setDenominacio("Concert de primavera");
        ev2.setDataInici("Dimarts");
        ev2.setUbicacio("Barcelona");


        Assertions.assertEquals(ev1, ev2);
    }

    @Test
    void testNotEqualsVoidValues() {
        Event ev1 = new Event();
        ev1.setDenominacio("Concert de primavera");
        ev1.setDataInici("Dimarts");
        ev1.setUbicacio("Barcelona");
        ev1.setAdreca("C/ Quinta Forca");

        Event ev2 = new Event();
        ev2.setDenominacio("Concert de primavera");
        ev2.setDataInici("Dimarts");
        ev2.setUbicacio("Barcelona");
        ev2.setEspai("Sideral");

        Assertions.assertNotEquals(ev1, ev2);
    }

    @Test
    void testHashEquals() {
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
        ev2.setAdreca("C/ Quinta Forca");
        ev2.setEspai("Sideral");

        Assertions.assertEquals(ev1.hashCode(), ev2.hashCode());
    }

    @Test
    void testHashNotEqual() {
        Event ev1 = new Event();
        ev1.setDenominacio("Concert de primavera");
        ev1.setDataInici("Dimarts");
        ev1.setUbicacio("Barcelona");
        ev1.setAdreca("C/ Quinta Forca");
        ev1.setEspai("Sideral");

        Event ev2 = new Event();
        ev2.setDenominacio("Concert de tardor");
        ev2.setDataInici("Dimarts");
        ev2.setUbicacio("Barcelona");
        ev2.setAdreca("C/ Quinta Forca");
        ev2.setEspai("Sideral");

        int h1 = ev1.hashCode();
        int h2 = ev2.hashCode();

        Assertions.assertNotEquals(ev1.hashCode(), ev2.hashCode());
    }

    @Test
    void updateEventOk() {
        Event ev1 = new Event();
        ev1.setDenominacio("Concert de primavera");
        ev1.setDataInici("Dimarts");
        ev1.setUbicacio("Barcelona");
        ev1.setAdreca("C/ Quinta Forca");
        ev1.setEspai("Sideral");

        Event ev2 = new Event();
        ev2.setDenominacio("Concert de primavera");
        ev2.setDataInici("Dimarts");
        ev2.setUbicacio("Tarragona");
        ev2.setAdreca("C/ Quinta Forca");
        ev2.setEspai("Sideral");

        ev1.update(ev2);

        Assertions.assertEquals(ev2,ev1);
    }

    @Test
    void updateEventOkMissingPars() {
        Event ev1 = new Event();
        ev1.setId(123L);
        ev1.setDenominacio("Concert de primavera");
        ev1.setDescripcio("Una bona desc");
        ev1.setDataInici("Dimarts");
        ev1.setUbicacio("Barcelona");
        ev1.setAdreca("C/ Quinta Forca");
        ev1.setEspai("Sideral");

        Event ev2 = new Event();

        ev2.setUbicacio("Tarragona");
        ev2.setAdreca("C/ Quarta Forca");


        Event res = new Event();
        res.setId(123L);
        res.setDenominacio("Concert de primavera");
        res.setDescripcio("Una bona desc");
        res.setDataInici("Dimarts");
        res.setUbicacio("Tarragona");
        res.setAdreca("C/ Quarta Forca");
        res.setEspai("Sideral");


        ev1.update(ev2);

        Assertions.assertEquals(res,ev1);
    }


}
