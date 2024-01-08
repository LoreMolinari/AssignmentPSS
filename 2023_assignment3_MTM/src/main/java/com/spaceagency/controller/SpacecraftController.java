package com.spaceagency.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spaceagency.model.Mission;
import com.spaceagency.model.Spacecraft;
import com.spaceagency.service.MissionService;
import com.spaceagency.service.SpacecraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/spacecrafts")
public class SpacecraftController {

    private final SpacecraftService spacecraftService;
    private final MissionService missionService;

    @Autowired
    public SpacecraftController(SpacecraftService spacecraftService, MissionService missionService) {
        this.spacecraftService = spacecraftService;
        this.missionService = missionService;
    }

    @GetMapping
    public List<Spacecraft> getAllSpacecrafts() {
        List<Spacecraft> spacecrafts = spacecraftService.getAllSpacecrafts();
        return spacecrafts.stream()
                .map(Spacecraft::toView)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Spacecraft getSpacecraftById(@PathVariable Long id) {
        Spacecraft spacecraft = spacecraftService.getSpacecraftById(id);
        return spacecraft.toView();
    }

    @PostMapping
    public Spacecraft saveSpacecraft(@RequestBody Spacecraft spacecraft) {
        Spacecraft savedSpacecraft = spacecraftService.saveSpacecraft(spacecraft);
        return savedSpacecraft.toView();
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveSpacecraftWeb(@RequestBody String spacecraftJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode spacecraftNode = objectMapper.readTree(spacecraftJson);

            String name = spacecraftNode.get("name").asText();
            String spaceAgency = spacecraftNode.get("agency").asText();

            Spacecraft spacecraft = new Spacecraft(name, spaceAgency);

            spacecraft.setMissions(null);

            Spacecraft savedSpacecraft = spacecraftService.saveSpacecraft(spacecraft);

            String savedSpacecraftJson = objectMapper.writeValueAsString(savedSpacecraft.toView());

            return ResponseEntity.status(HttpStatus.CREATED).body(savedSpacecraftJson);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving spacecraft");
        }
    }

    @GetMapping("/search/name/{name}")
    public List<Spacecraft> searchByName(@PathVariable String name) {
        List<Spacecraft> spacecrafts = spacecraftService.findByName(name);
        return spacecrafts.stream()
                .map(Spacecraft::toView)
                .collect(Collectors.toList());
    }

    @GetMapping("/search/spaceAgency/{spaceAgency}")
    public List<Spacecraft> searchBySpaceAgency(@PathVariable String spaceAgency) {
        List<Spacecraft> spacecrafts = spacecraftService.findBySpaceAgency(spaceAgency);
        return spacecrafts.stream()
                .map(Spacecraft::toView)
                .collect(Collectors.toList());
    }

    @GetMapping("/search/mission/{missionId}")
    public List<Spacecraft> searchByMissionId(@PathVariable Long missionId) {
        Mission mission = missionService.getMissionById(missionId);
        List<Spacecraft> spacecrafts = spacecraftService.findByMissionId(mission);
        return spacecrafts.stream()
                .map(Spacecraft::toView)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Spacecraft> updateSpacecraft(@RequestBody String updatedSpacecraftDataJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode spacecraftNode = objectMapper.readTree(updatedSpacecraftDataJson);

            String name = spacecraftNode.has("name") ? spacecraftNode.get("name").asText() : null;
            String spaceAgency = spacecraftNode.has("agency") ? spacecraftNode.get("agency").asText() : null;
            String id = spacecraftNode.has("id") ? spacecraftNode.get("id").asText() : null;

            Spacecraft existingSpacecraft = spacecraftService.getSpacecraftById(Long.valueOf(id));

            if (existingSpacecraft != null) {
                if (name != null) {
                    existingSpacecraft.setName(name);
                }

                if (spaceAgency != null) {
                    existingSpacecraft.setSpaceAgency(spaceAgency);
                }

                Spacecraft updatedSpacecraft = spacecraftService.saveSpacecraft(existingSpacecraft);

                return ResponseEntity.ok(updatedSpacecraft.toView());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpacecraft(@PathVariable Long id) {
        try {
            dissociateSpacecraftFromMissions(id);

            spacecraftService.deleteSpacecraft(id);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public void dissociateSpacecraftFromMissions(Long spacecraftId) {
        Spacecraft spacecraft = spacecraftService.getSpacecraftById(spacecraftId);

        for (Mission mission : spacecraft.getMissions()) {
            mission.setSpacecraft(null);
            missionService.saveMission(mission);
        }

        spacecraft.getMissions().clear();

        spacecraftService.saveSpacecraft(spacecraft);
    }

}
