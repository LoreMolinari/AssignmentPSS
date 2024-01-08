package com.spaceagency.spaceagency.test;

import com.spaceagency.controller.MissionController;
import com.spaceagency.controller.MissionLogsController;
import com.spaceagency.model.Mission;
import com.spaceagency.model.MissionLogs;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MissionLogsTest {

    @Autowired
    private MissionLogsController missionLogsController;
    @Autowired
    private MissionController missionController;

    private MissionLogs missionLogs;

    @BeforeEach
    public void setUp() {
        Mission savedMission = missionController.saveMission(new Mission("Apollo11", 65, LocalDate.parse("2022-01-01")));
        missionLogs = new MissionLogs("Log missione Apollo11", savedMission);
    }


    @Test
    @Order(1)
    public void testGetAllMissionLogs() {
        Mission savedMission = missionController.saveMission(new Mission("Artemis1", 60, LocalDate.parse("2021-01-01")));

        MissionLogs missionLog1 = new MissionLogs("Liftoff", savedMission);
        MissionLogs missionLog2 = new MissionLogs("Landed", savedMission);

        missionLogsController.saveMissionLogs(missionLog1);
        missionLogsController.saveMissionLogs(missionLog2);

        List<MissionLogs> result = missionLogsController.getAllMissionLogs();

        assertEquals(2, result.size());
        assertEquals("Liftoff", result.get(0).getLogMessage());
        assertEquals("Landed", result.get(1).getLogMessage());
    }

    @Test
    @Order(2)
    public void testGetMissionLogById() {
        MissionLogs result = missionLogsController.getMissionLogById(1L);

        assertEquals("Liftoff", result.getLogMessage());
    }

    @Test
    @Order(3)
    public void testSaveMissionLog() {
        MissionLogs result = missionLogsController.saveMissionLogs(missionLogs);

        assertEquals("Log missione Apollo11", result.getLogMessage());
    }

    @Test
    @Order(4)
    public void testDeleteMissionLog() {
        MissionLogs savedMissionLog = missionLogsController.saveMissionLogs(missionLogs);

        missionLogsController.deleteMissionLogs(savedMissionLog.getId());

        missionLogsController.getMissionLogById(savedMissionLog.getId());
    }
}
