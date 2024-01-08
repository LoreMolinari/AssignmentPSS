package com.spaceagency.spaceagency.test;

import com.spaceagency.controller.SpacesuitController;
import com.spaceagency.model.Spacesuit;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SpacesuitTest {

    @Autowired
    private SpacesuitController spacesuitController;

    private Spacesuit spacesuit;

    @BeforeEach
    public void setUp() {
        spacesuit = new Spacesuit("ModelX");
    }

    @Test
    @Order(1)
    public void testGetAllSpacesuits() {
        Spacesuit spacesuit1 = new Spacesuit("ModelX");
        Spacesuit spacesuit2 = new Spacesuit("Explorer");

        spacesuitController.saveSpacesuit(spacesuit1);
        spacesuitController.saveSpacesuit(spacesuit2);

        List<Spacesuit> result = spacesuitController.getAllSpacesuits();

        assertEquals(2, result.size());
        assertEquals("ModelX", result.get(0).getModel());
        assertEquals("Explorer", result.get(1).getModel());
    }

    @Test
    @Order(2)
    public void testGetSpacesuitById() {
        Spacesuit spacesuit1 = new Spacesuit("ModelX");

        spacesuitController.saveSpacesuit(spacesuit1);

        Spacesuit result = spacesuitController.getSpacesuitById(3L);

        assertEquals("ModelX", result.getModel());
    }

    @Test
    @Order(3)
    public void testSaveSpacesuit() {
        Spacesuit result = spacesuitController.saveSpacesuit(spacesuit);

        assertEquals("ModelX", result.getModel());
    }

    @Test
    @Order(4)
    public void testDeleteSpacesuit() {
        spacesuitController.deleteSpacesuit(1L);
    }
}
