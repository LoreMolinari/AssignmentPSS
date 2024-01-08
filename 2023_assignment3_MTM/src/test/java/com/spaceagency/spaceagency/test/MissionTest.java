package com.spaceagency.spaceagency.test;

import com.spaceagency.controller.MissionController;
import com.spaceagency.model.Mission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MissionTest {

    @Autowired
    private MissionController missionController;

    private Mission mission;

    @DirtiesContext
    @BeforeEach
    public void setUp() {
        mission = new Mission("Apollo 11", 60, LocalDate.parse("2023-02-01"));
    }

    @DirtiesContext
    @Test
    @Order(1)
    public void testGetAllMissions() {
        Mission mission1 = new Mission("Apollo 11", 65, LocalDate.parse("2023-02-01"));
        Mission mission2 = new Mission("Mars Rover", 150, LocalDate.parse("2023-01-01"));

        missionController.saveMission(mission1);
        missionController.saveMission(mission2);

        List<Mission> result = missionController.getAllMissions();

        assertEquals(4, result.size());
        assertEquals("Apollo 11", result.get(2).getName());
        assertEquals("Mars Rover", result.get(3).getName());
    }

    @Test
    @Order(2)
    public void testGetMissionById() {
        Mission mission1 = new Mission("Apollo 11", 65, LocalDate.parse("2023-02-01"));

        missionController.saveMission(mission1);

        Mission result = missionController.getMissionById(1L);

        assertEquals("Apollo 11", result.getName());
    }

    @Test
    @Order(3)
    public void testSaveMission() {
        Mission result = missionController.saveMission(mission);

        assertEquals("Apollo 11", result.getName());
    }

    @Test
    @Order(4)
    public void testDeleteMission() {
        missionController.deleteMission(1L);
    }
}
