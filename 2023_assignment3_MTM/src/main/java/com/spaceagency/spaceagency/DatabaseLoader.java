package com.spaceagency.spaceagency;

import com.spaceagency.controller.*;
import com.spaceagency.model.*;
import org.springframework.boot.CommandLineRunner;

import java.time.LocalDate;
import java.util.List;

//@Component
public class DatabaseLoader implements CommandLineRunner {

    private final AstronautController astronautController;
    private final SpacecraftController spacecraftController;
    private final MissionController missionController;
    private final MissionLogsController missionLogsController;
    private final SpacesuitController spacesuitController;

    public DatabaseLoader(
            AstronautController astronautController,
            SpacecraftController spacecraftController,
            MissionController missionController, MissionLogsController missionLogsController, SpacesuitController spacesuitController) {
        this.astronautController = astronautController;
        this.spacecraftController = spacecraftController;
        this.missionController = missionController;
        this.missionLogsController = missionLogsController;
        this.spacesuitController = spacesuitController;
    }

    @Override
    public void run(String... args) {
        System.out.println("Running DatabaseLoader to populate the database...");
        populateDatabase();
        System.out.println("DatabaseLoader finished.");
    }

    private void populateDatabase() {

        // Popolazione stronauti
        Astronaut astronaut1 = new Astronaut("Mario", "Rossi", 1980, "Italy");
        astronautController.saveAstronaut(astronaut1);

        Astronaut astronaut2 = new Astronaut("Giovanni", "Bianchi", 1990, "USA");
        astronautController.saveAstronaut(astronaut2);

        // Popolazione tute spaziali
        Spacesuit spacesuit1 = new Spacesuit("Modello1");
        spacesuit1.setAstronaut(astronaut1);
        spacesuitController.saveSpacesuit(spacesuit1);
        astronaut1.setSpacesuit(spacesuit1);
        astronautController.saveAstronaut(astronaut1);

        Spacesuit spacesuit2 = new Spacesuit("Modello2");
        spacesuit2.setAstronaut(astronaut2);
        spacesuitController.saveSpacesuit(spacesuit2);
        astronaut2.setSpacesuit(spacesuit2);
        astronautController.saveAstronaut(astronaut2);

        // Popolazione navi spaziali
        Spacecraft spacecraft1 = new Spacecraft("Apollo", "ESA");
        spacecraftController.saveSpacecraft(spacecraft1);

        Spacecraft spacecraft2 = new Spacecraft("Artemis", "NASA");
        spacecraftController.saveSpacecraft(spacecraft2);

        // Popolazione missioni
        Mission mission1 = new Mission("Apollo11", 10, LocalDate.parse("2023-01-01"));
        mission1.setAstronauts(List.of(astronaut1, astronaut2));
        mission1.setSpacecraft(spacecraft1);
        missionController.saveMission(mission1);

        Mission mission2 = new Mission("Artemis1", 15, LocalDate.parse("2023-02-01"));
        mission2.setAstronauts(List.of(astronaut2));
        mission2.setSpacecraft(spacecraft2);
        missionController.saveMission(mission2);

        // Popolazione log Missioni
        MissionLogs missionLogs1 = new MissionLogs("Log message 1", mission1);
        missionLogsController.saveMissionLogs(missionLogs1);

        MissionLogs missionLogs2 = new MissionLogs("Log message 2", mission2);
        missionLogsController.saveMissionLogs(missionLogs2);

        MissionLogs missionLogs3 = new MissionLogs("Log message 3", mission1);
        missionLogsController.saveMissionLogs(missionLogs3);

        // Popolazione collaboratori
        Astronaut astronaut3 = new Astronaut("Rosa", "Verdi", 1976, "France");
        astronautController.saveAstronaut(astronaut3);
        Astronaut astronaut4 = new Astronaut("Francesca", "Vergnano", 1994, "Germany");
        astronautController.saveAstronaut(astronaut4);

        astronaut2.setCollaborators(List.of(astronaut3));
        astronautController.saveAstronaut(astronaut2);

        astronaut3.setCollaborators(List.of(astronaut4, astronaut2));
        astronautController.saveAstronaut(astronaut3);

        astronaut4.setCollaborators(List.of(astronaut3));
        astronautController.saveAstronaut(astronaut4);

    }
}
