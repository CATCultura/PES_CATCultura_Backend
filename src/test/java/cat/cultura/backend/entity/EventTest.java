package cat.cultura.backend.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        Map<String,Object> ev2 = new HashMap<>();
        ev2.put("denominacio", "Concert de primavera");
        ev2.put("dataInici", "Dimarts");
        ev2.put("ubicacio", "Tarragona");
        ev2.put("adreca", "C/ Quinta Forca");
        ev2.put("espai","Sideral");

        Event result = new Event();
        result.setDenominacio("Concert de primavera");
        result.setDataInici("Dimarts");
        result.setUbicacio("Tarragona");
        result.setAdreca("C/ Quinta Forca");
        result.setEspai("Sideral");

        ev1.update(ev2);

        Assertions.assertEquals(result,ev1);
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

        Map<String,Object> ev2 = new HashMap<>();

        ev2.put("ubicacio","Tarragona");
        ev2.put("adreca","C/ Quarta Forca");


        Event expected = new Event();
        expected.setId(123L);
        expected.setDenominacio("Concert de primavera");
        expected.setDescripcio("Una bona desc");
        expected.setDataInici("Dimarts");
        expected.setUbicacio("Tarragona");
        expected.setAdreca("C/ Quarta Forca");
        expected.setEspai("Sideral");


        ev1.update(ev2);

        Assertions.assertEquals(expected,ev1);
    }

    @Test
    void updateEventOkCancelado() {
        Event ev1 = new Event();
        ev1.setId(123L);
        ev1.setDenominacio("Concert de primavera");
        ev1.setDescripcio("Una bona desc");
        ev1.setDataInici("Dimarts");
        ev1.setUbicacio("Barcelona");
        ev1.setAdreca("C/ Quinta Forca");
        ev1.setEspai("Sideral");
        ev1.setCancelado(false);

        Map<String,Object> ev2 = new HashMap<>();

        ev2.put("ubicacio","Tarragona");
        ev2.put("adreca","C/ Quarta Forca");
        ev2.put("cancelado",true);


        Event expected = new Event();
        expected.setId(123L);
        expected.setDenominacio("Concert de primavera");
        expected.setDescripcio("Una bona desc");
        expected.setDataInici("Dimarts");
        expected.setUbicacio("Tarragona");
        expected.setAdreca("C/ Quarta Forca");
        expected.setEspai("Sideral");
        expected.setCancelado(true);


        ev1.update(ev2);

        Assertions.assertEquals(expected,ev1);
        Assertions.assertEquals(expected.getCancelado(),ev1.getCancelado());
    }


}
