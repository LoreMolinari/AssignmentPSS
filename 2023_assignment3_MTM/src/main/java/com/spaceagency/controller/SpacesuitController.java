package com.spaceagency.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spaceagency.model.Astronaut;
import com.spaceagency.model.Spacesuit;
import com.spaceagency.service.AstronautService;
import com.spaceagency.service.SpacesuitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/spacesuits")
public class SpacesuitController {

    private final SpacesuitService spacesuitService;
    private final AstronautService astronautService;

    @Autowired
    public SpacesuitController(SpacesuitService spacesuitService, AstronautService astronautService) {
        this.spacesuitService = spacesuitService;
        this.astronautService = astronautService;
    }

    @GetMapping
    public List<Spacesuit> getAllSpacesuits() {
        return spacesuitService.getAllSpacesuits();
    }

    @GetMapping("/{id}")
    public Spacesuit getSpacesuitById(@PathVariable Long id) {
        return spacesuitService.getSpacesuitById(id);
    }

    @GetMapping("/search/name/{name}")
    public List<Spacesuit> searchByName(@PathVariable String name) {
        List<Spacesuit> spacesuits = spacesuitService.getSpacesuitByName(name);
        return spacesuits.stream()
                .map(Spacesuit::toView)
                .collect(Collectors.toList());
    }

    @PostMapping
    public Spacesuit saveSpacesuit(@RequestBody Spacesuit spacesuit) {
        return spacesuitService.saveSpacesuit(spacesuit);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveAstronautWeb(@RequestBody String spacesuitJson) {
        System.out.println(spacesuitJson);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode spacesuitNode = objectMapper.readTree(spacesuitJson);

            String model = spacesuitNode.get("model").asText();
            String astronautId = spacesuitNode.get("AId").asText();

            Astronaut astronaut = astronautService.getAstronautById(Long.valueOf(astronautId));

            Spacesuit spacesuit = new Spacesuit(model);
            spacesuitService.saveSpacesuit(spacesuit);

            astronaut.setSpacesuit(spacesuit);
            spacesuit.setAstronaut(astronaut);

            astronautService.saveAstronaut(astronaut);

            String savedSpacesuitJson = objectMapper.writeValueAsString(spacesuit.toView());

            return ResponseEntity.status(HttpStatus.CREATED).body(savedSpacesuitJson);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving spacesuit");
        }
    }

    @GetMapping("/search/spacesuit/{astronautId}")
    public ResponseEntity<Spacesuit> searchSpacesuitByAstronautId(@PathVariable Long astronautId) {
        Astronaut astronaut = astronautService.getAstronautById(astronautId);

        if (astronaut != null) {
            Spacesuit spacesuit = astronaut.getSpacesuit();

            if (spacesuit != null) {
                return ResponseEntity.ok(spacesuit.toView());
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpacesuit(@PathVariable Long id) {
        try {
            spacesuitService.deleteSpacesuit(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Spacesuit> updateSpacesuit(@RequestBody String updatedSpacesuitDataJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode spacesuitNode = objectMapper.readTree(updatedSpacesuitDataJson);


            String model = spacesuitNode.has("model") ? spacesuitNode.get("model").asText() : null;


            Spacesuit existingSpacesuit = spacesuitService.getSpacesuitById(Long.valueOf(spacesuitNode.get("id").asText()));

            if (existingSpacesuit != null) {
                if (model != null) {
                    existingSpacesuit.setModel(model);
                }

                Spacesuit updatedSpacesuit = spacesuitService.saveSpacesuit(existingSpacesuit);
                return ResponseEntity.ok(updatedSpacesuit);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
