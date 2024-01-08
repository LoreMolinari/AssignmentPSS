package com.spaceagency.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spaceagency.model.Mission;
import com.spaceagency.model.MissionLogs;
import com.spaceagency.service.MissionLogsService;
import com.spaceagency.service.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/missionlogs")
public class MissionLogsController {

    private final MissionLogsService missionLogsService;
    private final MissionService missionService;

    @Autowired
    public MissionLogsController(MissionLogsService missionLogsService, MissionService missionService) {
        this.missionLogsService = missionLogsService;
        this.missionService = missionService;
    }

    @GetMapping
    public List<MissionLogs> getAllMissionLogs() {
        return missionLogsService.getAllMissionLogs();
    }

    @GetMapping("/mission/{missionId}")
    public List<MissionLogs> getMissionLogsByMissionId(@PathVariable Long missionId) {
        return missionLogsService.getMissionLogsByMissionId(missionId);
    }

    @PostMapping
    public MissionLogs saveMissionLogs(@RequestBody MissionLogs missionLogs) {
        return missionLogsService.saveMissionLogs(missionLogs);
    }

    @GetMapping("/{id}")
    public MissionLogs getMissionLogById(@PathVariable Long id) {
        return missionLogsService.getMissionLogById(id);
    }

    @GetMapping("/missionByLogId/{missionLogId}")
    public Optional<Mission> getMissionByMissionLogId(@PathVariable Long missionLogId) {
        return missionLogsService.getMissionByMissionLogId(missionLogId);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveMissionLogsWeb(@RequestBody String missionLogsJson) {
        System.out.println(missionLogsJson);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode missionLogsNode = objectMapper.readTree(missionLogsJson);

            String logMessage = missionLogsNode.get("logMessage").asText();
            String missionIdAsString = missionLogsNode.get("missionId").asText();

            Long missionId = Long.parseLong(missionIdAsString);

            Mission mission = missionService.getMissionById(missionId);

            MissionLogs missionLogs = new MissionLogs(logMessage, mission);
            MissionLogs savedMissionLogs = missionLogsService.saveMissionLogs(missionLogs);

            String savedMissionLogsJson = objectMapper.writeValueAsString(savedMissionLogs.toView());

            return ResponseEntity.status(HttpStatus.CREATED).body(savedMissionLogsJson);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving mission logs");
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMissionLogs(@PathVariable Long id) {
        try {
            missionLogsService.deleteMissionLogsById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MissionLogs> updateMissionLogs(@PathVariable Long id, @RequestBody MissionLogs updatedMissionLogs) {
        try {
            MissionLogs existingMissionLogs = missionLogsService.getMissionLogById(id);

            if (existingMissionLogs != null) {
                existingMissionLogs.setLogMessage(updatedMissionLogs.getLogMessage());

                MissionLogs updatedLogs = missionLogsService.saveMissionLogs(existingMissionLogs);

                return ResponseEntity.ok(updatedLogs.toView());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
