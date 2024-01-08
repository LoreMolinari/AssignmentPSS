package com.spaceagency.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spaceagency.model.Mission;
import com.spaceagency.model.MissionLogs;
import com.spaceagency.service.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/missions")
public class MissionController {

    private final MissionService missionService;

    @Autowired
    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }

    @GetMapping
    public List<Mission> getAllMissions() {
        List<Mission> missions = missionService.getAllMissions();
        return missions.stream()
                .map(Mission::toView)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Mission getMissionById(@PathVariable Long id) {
        Mission mission = missionService.getMissionById(id);

        if (mission != null) {
            return mission.toView();
        } else {
            return null;
        }
    }


    @PostMapping
    public Mission saveMission(@RequestBody Mission mission) {
        return missionService.saveMission(mission);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMission(@PathVariable Long id) {
        try {
            dissociate(id);

            missionService.deleteMission(id);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public void dissociate(Long missionId) {
        Mission mission = missionService.getMissionById(missionId);

        for (MissionLogs missionLog : mission.getMissionLogs()) {
            missionLog.setMission(null);
        }
        mission.getMissionLogs().clear();

        missionService.saveMission(mission);
    }

    @GetMapping("/search/name/{name}")
    public List<Mission> searchByLaunchDate(@PathVariable("name") String name) {
        return missionService.findByName(name);
    }

    @GetMapping("/search/launch-date/{launchDate}")
    public List<Mission> searchByLaunchDate(@PathVariable("launchDate") LocalDate launchDate) {
        return missionService.findByLaunchDate(launchDate);
    }

    @GetMapping("/search/spacecraft/{spacecraftId}")
    public List<Mission> searchBySpacecraft(@PathVariable Long spacecraftId) {
        return missionService.findBySpacecraft(spacecraftId);
    }

    @GetMapping("/search/astronaut/{astronautId}")
    public List<Mission> searchByAstronaut(@PathVariable Long astronautId) {
        return missionService.findByAstronaut(astronautId);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveMissionWeb(@RequestBody String missionJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode missionNode = objectMapper.readTree(missionJson);

            String name = missionNode.get("name").asText();
            Integer duration = Integer.parseInt(missionNode.get("duration").asText());
            ;
            String launchDateString = missionNode.get("launchDate").asText();
            LocalDate launchDate = LocalDate.parse(launchDateString);

            Mission mission = new Mission(name, duration, launchDate);

            mission.setMissionLogs(null);
            mission.setAstronauts(null);
            mission.setSpacecraft(null);

            Mission savedMission = missionService.saveMission(mission);
            String savedMissionJson = objectMapper.writeValueAsString(savedMission.toView());

            return ResponseEntity.status(HttpStatus.CREATED).body(savedMissionJson);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving mission");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mission> updateMission(@PathVariable Long id, @RequestBody String updatedMissionDataJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode missionNode = objectMapper.readTree(updatedMissionDataJson);

            String name = missionNode.has("name") ? missionNode.get("name").asText() : null;
            Integer duration = missionNode.has("duration") ? missionNode.get("duration").asInt() : null;
            String launchDate = missionNode.has("launchDate") ? missionNode.get("launchDate").asText() : null;

            Mission existingMission = missionService.getMissionById(id);

            if (existingMission != null) {
                if (name != null) {
                    existingMission.setName(name);
                }

                if (duration != null) {
                    existingMission.setDuration(duration);
                }

                if (launchDate != null) {
                    existingMission.setLaunchDate(LocalDate.parse(launchDate));
                }

                existingMission.setMissionLogs(null);
                existingMission.setAstronauts(null);
                existingMission.setSpacecraft(null);

                Mission updatedMissionEntity = missionService.saveMission(existingMission);

                return ResponseEntity.ok(updatedMissionEntity.toView());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
