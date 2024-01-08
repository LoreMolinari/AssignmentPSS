package com.spaceagency.spaceagency;

import com.spaceagency.controller.*;
import com.spaceagency.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
@EntityScan("com.spaceagency.model")
@EnableJpaRepositories("com.spaceagency.repository")
@ComponentScan("com.spaceagency")
public class SpaceAgencyApplication implements CommandLineRunner {

    private final AstronautController astronautController;
    private final SpacecraftController spacecraftController;
    private final MissionController missionController;
    private final MissionLogsController missionLogsController;
    private final SpacesuitController spacesuitController;

    @Autowired
    public SpaceAgencyApplication(
            AstronautController astronautController,
            SpacecraftController spacecraftController,
            MissionController missionController,
            MissionLogsController missionLogsController,
            SpacesuitController spacesuitController) {
        this.astronautController = astronautController;
        this.spacecraftController = spacecraftController;
        this.missionController = missionController;
        this.missionLogsController = missionLogsController;
        this.spacesuitController = spacesuitController;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpaceAgencyApplication.class, args);
    }

    @Override
    public void run(String... args) {
        DatabaseLoader dbL = new DatabaseLoader(astronautController, spacecraftController, missionController, missionLogsController, spacesuitController);
        //dbL.run();
        //runMenu();
    }

    public void runMenu() {
        Scanner inputScanner = new Scanner(System.in);
        int choice = 0;

        do {
            System.out.println("===== Menu di selezione =====");
            System.out.println("1. Astronaut");
            System.out.println("2. Spacecraft");
            System.out.println("3. Mission");
            System.out.println("4. MissionLogs");
            System.out.println("5. Spacesuit");
            System.out.println("6. Close");

            System.out.print("Inserisci la tua scelta: ");

            if (inputScanner.hasNextInt()) {
                choice = inputScanner.nextInt();
                inputScanner.nextLine();
            } else {
                System.out.println("Input non valido. Inserisci un intero.");
                inputScanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    handleAstronautMenu();
                    break;
                case 2:
                    handleSpacecraftMenu();
                    break;
                case 3:
                    handleMissionMenu();
                    break;
                case 4:
                    handleMissionLogsMenu();
                    break;
                case 5:
                    handleSpacesuitMenu();
                case 6:
                    System.out.println("Program Closed, Goodbye!");
                    break;
                default:
                    System.out.println("Numero non valido, riprova con il numero corretto!");
                    break;
            }

        } while (choice != 6);
        inputScanner.close();
    }

    private void handleAstronautMenu() {
        Scanner inputScanner = new Scanner(System.in);
        int astronautChoice;

        do {
            System.out.println("===== Menu Astronaut =====");
            System.out.println("1. Visualizza tutti gli astronauti");
            System.out.println("2. Aggiungi un nuovo astronauta");
            System.out.println("3. Visualizza i collaboratori");
            System.out.println("4. Torna al menu principale");

            System.out.print("Inserisci la tua scelta: ");
            astronautChoice = inputScanner.nextInt();

            switch (astronautChoice) {
                case 1:
                    displayAllAstronauts();
                    break;
                case 2:
                    addNewAstronaut();
                    break;
                case 3:
                    displayCollaborators();
                    break;
                case 4:
                    System.out.println("Tornando al menu principale.");
                    break;
                default:
                    System.out.println("Numero non valido, riprova con il numero corretto!");
                    break;
            }

        } while (astronautChoice != 4);
    }

    private void displayAllAstronauts() {
        List<Astronaut> astronauts = astronautController.getAllAstronauts();
        System.out.println("===== Tutti gli astronauti =====");
        for (Astronaut astronaut : astronauts) {
            System.out.println(astronaut);
        }
    }

    private void addNewAstronaut() {
        Scanner inputScanner = new Scanner(System.in);

        System.out.println("===== Aggiungi un nuovo astronauta =====");
        System.out.print("Inserisci il nome dell'astronauta: ");
        String name = inputScanner.nextLine();

        System.out.print("Inserisci il cognome dell'astronauta: ");
        String surname = inputScanner.nextLine();

        System.out.print("Inserisci l'anno di nascita dell'astronauta: ");
        Integer birthyear = Integer.valueOf(inputScanner.nextLine());

        System.out.print("Inserisci la nazionalità dell'astronauta: ");
        String nationality = inputScanner.nextLine();

        Astronaut newAstronaut = new Astronaut(name, surname, birthyear, nationality);

        astronautController.saveAstronaut(newAstronaut);

        System.out.println("Astronauta aggiunto con successo!");
    }

    private void displayCollaborators() {
        Scanner inputScanner = new Scanner(System.in);

        System.out.print("Inserisci l'ID dell'astronauta per visualizzare i collaboratori: ");
        Long astronautId = inputScanner.nextLong();

        Astronaut astronaut = astronautController.getAstronautById(astronautId);
        List<Astronaut> collaborators = astronautController.getCollaborators(astronautId);

        astronaut.getCollaborators().size();

        if (collaborators != null) {
            System.out.println("===== Collaboratori di " + astronaut.getName() + " " + astronaut.getSurname() + " =====");
            for (Astronaut collaborator : collaborators) {
                System.out.println(collaborator);
            }
        } else {
            System.out.println("Astronauta non trovato con l'ID specificato.");
        }
    }


    private void handleSpacecraftMenu() {
        Scanner inputScanner = new Scanner(System.in);
        int astronautChoice;

        do {
            System.out.println("===== Menu Astronaut =====");
            System.out.println("1. Visualizza tutte le navicelle spaziali");
            System.out.println("2. Aggiungi una nuova navicella");
            System.out.println("3. Torna al menu principale");

            System.out.print("Inserisci la tua scelta: ");
            astronautChoice = inputScanner.nextInt();

            switch (astronautChoice) {
                case 1:
                    displayAllSpacecraft();
                    break;
                case 2:
                    addNewSpacecraft();
                    break;
                case 3:
                    System.out.println("Tornando al menu principale.");
                    break;
                default:
                    System.out.println("Numero non valido, riprova con il numero corretto!");
                    break;
            }

        } while (astronautChoice != 3);
    }

    private void displayAllSpacecraft() {
        List<Spacecraft> spacecrafts = spacecraftController.getAllSpacecrafts();
        System.out.println("===== Tutte le astronavi =====");
        for (Spacecraft spacecraft : spacecrafts) {
            System.out.println(spacecraft);
        }
    }

    private void addNewSpacecraft() {
        Scanner inputScanner = new Scanner(System.in);

        System.out.println("===== Aggiungi una nuova navicella =====");
        System.out.print("Inserisci il nome: ");
        String name = inputScanner.nextLine();

        System.out.print("Inserisci l'agenzia spaziale di appartenenza: ");
        String spaceAgency = inputScanner.nextLine();

        Spacecraft spacecraft = new Spacecraft(name, spaceAgency);

        spacecraftController.saveSpacecraft(spacecraft);

        System.out.println("Navicella aggiunta con successo!");
    }

    private void handleMissionMenu() {
        Scanner inputScanner = new Scanner(System.in);
        int missionChoice;

        do {
            System.out.println("===== Menu Mission =====");
            System.out.println("1. Visualizza tutte le missioni");
            System.out.println("2. Aggiungi una nuova missione");
            System.out.println("3. Cerca missioni per astronauta");
            System.out.println("4. Cerca missioni per data di lancio");
            System.out.println("5. Cerca missioni per navicella");
            System.out.println("6. Cerca missioni per ID");
            System.out.println("7. Torna al menu principale");

            System.out.print("Inserisci la tua scelta: ");
            missionChoice = inputScanner.nextInt();

            switch (missionChoice) {
                case 1:
                    displayAllMissions();
                    break;
                case 2:
                    addNewMission();
                    break;
                case 3:
                    searchMissionByAstronaut();
                    break;
                case 4:
                    searchMissionByLaunchDate();
                    break;
                case 5:
                    searchMissionBySpacecraft();
                    break;
                case 6:
                    searchMissionByID();
                    break;
                case 7:
                    System.out.println("Tornando al menu principale.");
                    break;
                default:
                    System.out.println("Numero non valido, riprova con il numero corretto!");
                    break;
            }

        } while (missionChoice != 7);
    }

    private void displayAllMissions() {
        List<Mission> missions = missionController.getAllMissions();
        System.out.println("===== Tutte le missioni =====");
        for (Mission mission : missions) {
            System.out.println(mission);
        }
    }

    private void addNewMission() {
        Scanner inputScanner = new Scanner(System.in);

        System.out.println("===== Aggiungi una nuova missione =====");
        System.out.print("Inserisci il nome della missione: ");
        String missionName = inputScanner.nextLine();

        System.out.print("Inserisci la durata della missione in giorni: ");
        int missionDuration = inputScanner.nextInt();

        System.out.print("Inserisci la data di lancio della missione (yyyy-MM-dd): ");
        String launchDateString = inputScanner.next();
        LocalDate launchDate = LocalDate.parse(launchDateString);

        Mission newMission = new Mission(missionName, missionDuration, launchDate);

        List<Astronaut> astronauts = new ArrayList<>();
        System.out.println("Inserisci gli ID degli astronauti coinvolti nella missione (inserisci 0 per terminare): ");
        long astronautId;
        do {
            astronautId = inputScanner.nextLong();
            if (astronautId > 0) {
                Astronaut astronaut = astronautController.getAstronautById(astronautId);
                if (astronaut != null) {
                    astronauts.add(astronaut);
                } else {
                    System.out.println("Astronaut with ID " + astronautId + " not found. Please enter a valid ID.");
                }
            }
        } while (astronautId > 0);

        newMission.setAstronauts(astronauts);

        System.out.print("Inserisci l'ID della navicella spaziale coinvolta nella missione: ");
        long spacecraftId = inputScanner.nextLong();

        if (spacecraftId > 0) {
            Spacecraft spacecraft = spacecraftController.getSpacecraftById(spacecraftId);
            newMission.setSpacecraft(spacecraft);
        }

        missionController.saveMission(newMission);

        System.out.println("Missione aggiunta con successo!");
    }

    private void searchMissionByLaunchDate() {
        Scanner inputScanner = new Scanner(System.in);

        System.out.print("Inserisci la data di lancio della missione (YYYY-MM-DD): ");
        String launchDateString = inputScanner.next();
        LocalDate launchDate = LocalDate.parse(launchDateString);

        List<Mission> missions = missionController.searchByLaunchDate(launchDate);
        System.out.println("===== Missioni per data di lancio =====");
        for (Mission mission : missions) {
            System.out.println(mission);
        }
    }

    private void searchMissionByAstronaut() {
        Scanner inputScanner = new Scanner(System.in);

        System.out.print("Inserisci l'ID dell'astronauta: ");
        Long astronautId = inputScanner.nextLong();

        List<Mission> missions = missionController.searchByAstronaut(astronautId);
        System.out.println("===== Missioni per Astronauta =====");
        for (Mission mission : missions) {
            System.out.println(mission);
        }
    }

    private void searchMissionBySpacecraft() {
        Scanner inputScanner = new Scanner(System.in);

        System.out.print("Inserisci l'ID dello spacecraft: ");
        Long spacecraftId = inputScanner.nextLong();

        List<Mission> missions = missionController.searchBySpacecraft(spacecraftId);
        System.out.println("===== Missioni per Spacecraft =====");
        for (Mission mission : missions) {
            System.out.println(mission);
        }
    }

    private void searchMissionByID() {
        Scanner inputScanner = new Scanner(System.in);

        System.out.print("Inserisci l'ID da cercare: ");
        Long missionID = inputScanner.nextLong();

        Mission mission = missionController.getMissionById(missionID);
        System.out.println("===== Missioni trovate =====");
        System.out.println(mission);
    }

    private void handleMissionLogsMenu() {
        Scanner inputScanner = new Scanner(System.in);
        int missionLogsChoice;

        do {
            System.out.println("===== Menu MissionLogs =====");
            System.out.println("1. Visualizza tutti i MissionLogs");
            System.out.println("2. Visualizza un MissionLog per ID");
            System.out.println("3. Visualizza i MissionLogs per Missione");
            System.out.println("4. Aggiungi un nuovo MissionLog");
            System.out.println("5. Elimina un MissionLog per ID");
            System.out.println("6. Torna al menu principale");

            System.out.print("Inserisci la tua scelta: ");
            missionLogsChoice = inputScanner.nextInt();

            switch (missionLogsChoice) {
                case 1:
                    displayAllMissionLogs();
                    break;
                case 2:
                    displayMissionLogById();
                    break;
                case 3:
                    displayMissionLogsByMission();
                    break;
                case 4:
                    addNewMissionLog();
                    break;
                case 5:
                    deleteMissionLogById();
                    break;
                case 6:
                    System.out.println("Tornando al menu principale.");
                    break;
                default:
                    System.out.println("Numero non valido, riprova con il numero corretto!");
                    break;
            }

        } while (missionLogsChoice != 6);
    }

    private void displayAllMissionLogs() {
        List<MissionLogs> missionLogsList = missionLogsController.getAllMissionLogs();
        System.out.println("===== Tutti i MissionLogs =====");
        for (MissionLogs missionLogs : missionLogsList) {
            System.out.println(missionLogs);
        }
    }

    private void displayMissionLogById() {
        Scanner inputScanner = new Scanner(System.in);
        System.out.print("Inserisci l'ID del MissionLog: ");
        Long missionLogId = inputScanner.nextLong();

        MissionLogs missionLog = missionLogsController.getMissionLogById(missionLogId);
        System.out.println("===== MissionLog per ID =====");
        System.out.println(missionLog);
    }

    private void displayMissionLogsByMission() {
        Scanner inputScanner = new Scanner(System.in);
        System.out.print("Inserisci l'ID della Missione: ");
        Long missionId = inputScanner.nextLong();

        List<MissionLogs> missionLogsList = missionLogsController.getMissionLogsByMissionId(missionId);
        System.out.println("===== MissionLogs per Missione =====");
        for (MissionLogs missionLogs : missionLogsList) {
            System.out.println(missionLogs);
        }
    }

    private void addNewMissionLog() {
        Scanner inputScanner = new Scanner(System.in);

        System.out.println("===== Aggiungi un nuovo MissionLog =====");
        System.out.print("Inserisci il messaggio del log: ");
        String logMessage = inputScanner.next();

        System.out.print("Inserisci l'ID della Missione associata al log: ");
        while (!inputScanner.hasNextLong()) {
            System.out.println("Inserisci un ID valido della Missione.");
            inputScanner.next();
        }
        Long missionId = inputScanner.nextLong();
        System.out.println(missionId);

        Mission mission = missionController.getMissionById(missionId);

        if (mission != null) {
            MissionLogs newMissionLog = new MissionLogs(logMessage, mission);
            missionLogsController.saveMissionLogs(newMissionLog);
            System.out.println("MissionLog aggiunto con successo!");
        } else {
            System.out.println("Nessuna Missione trovata con l'ID specificato. Impossibile aggiungere il MissionLog.");
        }
    }


    private void deleteMissionLogById() {
        Scanner inputScanner = new Scanner(System.in);
        System.out.print("Inserisci l'ID del MissionLog da eliminare: ");
        Long missionLogId = inputScanner.nextLong();

        missionLogsController.deleteMissionLogs(missionLogId);
        System.out.println("MissionLog eliminato con successo!");
    }

    private void handleSpacesuitMenu() {
        Scanner inputScanner = new Scanner(System.in);
        int spacesuitChoice;

        do {
            System.out.println("===== Menu Spacesuit =====");
            System.out.println("1. Visualizza tutti gli Spacesuit");
            System.out.println("2. Aggiungi un nuovo Spacesuit");
            System.out.println("3. Torna al menu principale");

            System.out.print("Inserisci la tua scelta: ");
            spacesuitChoice = inputScanner.nextInt();

            switch (spacesuitChoice) {
                case 1:
                    displayAllSpacesuits();
                    break;
                case 2:
                    addNewSpacesuit();
                    break;
                case 3:
                    System.out.println("Tornando al menu principale.");
                    runMenu();
                default:
                    System.out.println("Numero non valido, riprova con il numero corretto!");
                    break;
            }

        } while (spacesuitChoice != 3);
    }

    private void displayAllSpacesuits() {
        List<Spacesuit> spacesuits = spacesuitController.getAllSpacesuits();
        System.out.println("===== Tutti gli Spacesuit =====");
        for (Spacesuit spacesuit : spacesuits) {
            System.out.println(spacesuit);
        }
    }

    private void addNewSpacesuit() {
        Scanner inputScanner = new Scanner(System.in);

        System.out.println("===== Aggiungi un nuovo Spacesuit =====");
        System.out.print("Inserisci il modello dello Spacesuit: ");
        String model = inputScanner.nextLine();

        Astronaut astronaut = selectAstronautForSpacesuit();
        if (astronaut != null) {
            if (astronaut.getSpacesuit() == null) {
                Spacesuit spacesuit = new Spacesuit();
                spacesuit.setModel(model);
                spacesuit.setAstronaut(astronaut);

                spacesuitController.saveSpacesuit(spacesuit);
                astronaut.setSpacesuit(spacesuit);
                astronautController.saveAstronaut(astronaut);

                System.out.println("Spacesuit aggiunto con successo!");
            } else {
                System.out.println("L'astronauta ha già uno Spacesuit associato.");
            }
        } else {
            System.out.println("Nessun astronauta disponibile. Impossibile aggiungere lo Spacesuit.");
        }
    }


    private Astronaut selectAstronautForSpacesuit() {
        Scanner inputScanner = new Scanner(System.in);
        System.out.println("Seleziona un astronauta per lo Spacesuit (inserisci l'ID dell'astronauta): ");
        List<Astronaut> astronauts = astronautController.getAllAstronauts();
        for (Astronaut astronaut : astronauts) {
            System.out.println(astronaut);
        }

        System.out.print("Inserisci l'ID dell'astronauta: ");
        long astronautId = inputScanner.nextLong();

        return astronautController.getAstronautById(astronautId);
    }

}
