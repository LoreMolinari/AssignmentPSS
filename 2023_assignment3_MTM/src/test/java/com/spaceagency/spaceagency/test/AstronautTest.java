package com.spaceagency.spaceagency.test;

import com.spaceagency.controller.AstronautController;
import com.spaceagency.model.Astronaut;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AstronautTest {

    @Autowired
    private AstronautController astronautController;

    private Astronaut astronaut;

    @BeforeEach
    public void setUp() {
        astronaut = new Astronaut("Mario", "Rossi", 1985, "USA");
    }

    @Test
    @Order(1)
    public void testGetAllAstronauts() {
        Astronaut astronaut1 = new Astronaut("Mario", "Rossi", 1985, "USA");
        Astronaut astronaut2 = new Astronaut("Jane", "Austin", 1990, "Canada");

        astronautController.saveAstronaut(astronaut1);
        astronautController.saveAstronaut(astronaut2);

        List<Astronaut> result = astronautController.getAllAstronauts();


        assertEquals(2, result.size());
        assertEquals("Mario", result.get(0).getName());
        assertEquals("Jane", result.get(1).getName());
    }

    @Test
    @Order(2)
    public void testGetAstronautById() {
        Astronaut result = astronautController.getAstronautById(1L);

        assertEquals("Mario", result.getName());
    }

    @Test
    @Order(3)
    public void testGetCollaboratorsById() {
        Astronaut astronaut3 = new Astronaut("Rosa", "Verdi", 1976, "France");
        astronautController.saveAstronaut(astronaut3);
        Astronaut astronaut4 = new Astronaut("Francesca", "Vergnano", 1994, "Germany");
        astronautController.saveAstronaut(astronaut4);

        astronaut3.setCollaborators(List.of(astronaut4));
        astronautController.saveAstronaut(astronaut3);

        astronaut4.setCollaborators(List.of(astronaut3));
        astronautController.saveAstronaut(astronaut4);

        List<Astronaut> collaborators = astronautController.getCollaborators(3L);

        assertEquals("Francesca", collaborators.get(0).getName());
    }


    @Test
    @Order(4)
    public void testSaveAstronaut() {
        Astronaut result = astronautController.saveAstronaut(astronaut);

        assertEquals("Mario", result.getName());
    }

    @Test
    @Order(5)
    public void testDeleteAstronaut() {
        astronautController.deleteAstronaut(1L);
    }
}
