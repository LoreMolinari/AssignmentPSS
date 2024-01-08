package com.spaceagency.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spaceagency.model.Astronaut;
import com.spaceagency.model.Mission;
import com.spaceagency.service.AstronautService;
import com.spaceagency.service.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/astronauts")
public class AstronautController {

    private final AstronautService astronautService;
    private final MissionService missionService;

    @Autowired
    public AstronautController(AstronautService astronautService, MissionService missionService) {
        this.astronautService = astronautService;
        this.missionService = missionService;
    }

    @GetMapping
    public List<Astronaut> getAllAstronauts() {
        List<Astronaut> astronauts = astronautService.getAllAstronauts();
        return astronauts.stream()
                .map(Astronaut::toView)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Astronaut getAstronautById(@PathVariable Long id) {
        Astronaut astronaut = astronautService.getAstronautById(id);
        if (astronaut != null) {
            return astronaut.toView();
        } else {
            return null;
        }
    }


    @PostMapping
    public Astronaut saveAstronaut(@RequestBody Astronaut astronaut) {
        Astronaut savedAstronaut = astronautService.saveAstronaut(astronaut);
        return savedAstronaut.toView();
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveAstronautWeb(@RequestBody String astronautJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode astronautNode = objectMapper.readTree(astronautJson);

            String name = astronautNode.get("name").asText();
            String surname = astronautNode.get("surname").asText();
            String nationality = astronautNode.get("nationality").asText();
            Integer birthyear = astronautNode.get("birthyear").asInt();

            Astronaut astronaut = new Astronaut(name, surname, birthyear, nationality);

            astronaut.setCollaborators(null);
            astronaut.setMissions(null);

            Astronaut savedAstronaut = astronautService.saveAstronaut(astronaut);
            String savedAstronautJson = objectMapper.writeValueAsString(savedAstronaut.toView());

            return ResponseEntity.status(HttpStatus.CREATED).body(savedAstronautJson);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving astronaut");
        }
    }

    @GetMapping("/search/name/{name}")
    public List<Astronaut> searchByName(@PathVariable String name) {
        List<Astronaut> astronauts = astronautService.findByName(name);
        return astronauts.stream()
                .map(Astronaut::toView)
                .collect(Collectors.toList());
    }

    @GetMapping("/search/country/{nationality}")
    public List<Astronaut> searchByCountry(@PathVariable String nationality) {
        List<Astronaut> astronauts = astronautService.findByCountry(nationality);
        return astronauts.stream()
                .map(Astronaut::toView)
                .collect(Collectors.toList());
    }

    @GetMapping("/search/year/{year}")
    public List<Astronaut> searchByYear(@PathVariable String year) {
        List<Astronaut> astronauts = astronautService.findByYear(year);
        return astronauts.stream()
                .map(Astronaut::toView)
                .collect(Collectors.toList());
    }

    @GetMapping("/search/mission/{missionId}")
    public List<Astronaut> searchByMission(@PathVariable Long missionId) {
        Mission mission = missionService.getMissionById(missionId);
        List<Astronaut> astronauts = astronautService.findByMission(mission);
        return astronauts.stream()
                .map(Astronaut::toView)
                .collect(Collectors.toList());
    }

    @GetMapping("/collaborators/{astronautId}")
    public List<Astronaut> getCollaborators(@PathVariable Long astronautId) {
        List<Astronaut> collaborators = astronautService.getCollaborators(astronautId);

        collaborators.size();

        return collaborators.stream()
                .map(Astronaut::toView)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAstronaut(@PathVariable Long id) {
        try {
            dissociateAstronautFromMissions(id);

            astronautService.deleteAstronaut(id);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public void dissociateAstronautFromMissions(Long astronautId) {
        Astronaut astronaut = astronautService.getAstronautById(astronautId);

        astronaut.getMissions().clear();
        astronautService.saveAstronaut(astronaut);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Astronaut> updateAstronaut(@RequestBody String updatedAstronautDataJson) {
        System.out.println(updatedAstronautDataJson);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode astronautNode = objectMapper.readTree(updatedAstronautDataJson);

            String name = astronautNode.has("name") ? astronautNode.get("name").asText() : null;
            String surname = astronautNode.has("surname") ? astronautNode.get("surname").asText() : null;
            String nationality = astronautNode.has("nationality") ? astronautNode.get("nationality").asText() : null;
            String birthyear = astronautNode.has("birthyear") ? astronautNode.get("birthyear").asText() : null;
            String id = astronautNode.has("id") ? astronautNode.get("id").asText() : null;


            Astronaut existingAstronaut = astronautService.getAstronautById(Long.valueOf(id));

            if (existingAstronaut != null) {
                if (name != null) {
                    existingAstronaut.setName(name);
                }

                if (surname != null) {
                    existingAstronaut.setSurname(surname);
                }

                if (nationality != null) {
                    existingAstronaut.setNationality(nationality);
                }

                if (birthyear != null) {
                    existingAstronaut.setBirthyear(Integer.valueOf(birthyear));
                }

                Astronaut updatedAstronaut = astronautService.saveAstronaut(existingAstronaut);

                return ResponseEntity.ok(updatedAstronaut.toView());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
