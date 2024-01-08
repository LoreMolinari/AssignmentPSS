package com.spaceagency.spaceagency.test;

import com.spaceagency.controller.SpacecraftController;
import com.spaceagency.model.Spacecraft;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SpacecraftTest {

    @Autowired
    private SpacecraftController spacecraftController;

    private Spacecraft spacecraft;

    @BeforeEach
    public void setUp() {
        spacecraft = new Spacecraft("Apollo", "NASA");
    }

    @Test
    @Order(1)
    public void testGetAllSpacecrafts() {
        Spacecraft spacecraft1 = new Spacecraft("Apollo", "NASA");
        Spacecraft spacecraft2 = new Spacecraft("Mars Rover", "ESA");

        spacecraftController.saveSpacecraft(spacecraft1);
        spacecraftController.saveSpacecraft(spacecraft2);

        List<Spacecraft> result = spacecraftController.getAllSpacecrafts();

        assertEquals(2, result.size());
        assertEquals("Apollo", result.get(0).getName());
        assertEquals("Mars Rover", result.get(1).getName());
    }

    @Test
    @Order(2)
    public void testGetSpacecraftById() {
        Spacecraft result = spacecraftController.getSpacecraftById(1L);

        assertEquals("Apollo", result.getName());
    }

    @Test
    @Order(3)
    public void testSaveSpacecraft() {
        Spacecraft result = spacecraftController.saveSpacecraft(spacecraft);

        assertEquals("Apollo", result.getName());
    }

    @Test
    @Order(4)
    public void testDeleteSpacecraft() {
        spacecraftController.deleteSpacecraft(1L);
    }
}
