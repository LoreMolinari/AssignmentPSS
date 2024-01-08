package com.spaceagency.service;

import com.spaceagency.model.Astronaut;
import com.spaceagency.model.Mission;
import com.spaceagency.repository.MissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MissionService {

    private final MissionRepository missionRepository;

    @Autowired
    public MissionService(MissionRepository missionRepository) {
        this.missionRepository = missionRepository;
    }

    public List<Mission> getAllMissions() {
        return missionRepository.findAll();
    }

    public Mission getMissionById(Long id) {
        return missionRepository.findById(id).orElse(null);
    }

    public Mission saveMission(Mission mission) {
        return missionRepository.save(mission);
    }

    public void deleteMission(Long id) {
        missionRepository.deleteById(id);
    }

    public List<Mission> findByAstronaut(Long astronautId) {
        Astronaut astronaut = new Astronaut();
        astronaut.setId(astronautId);
        return missionRepository.findByAstronaut(astronaut.getId());
    }

    public List<Mission> findBySpacecraft(Long spacecraftId) {
        return missionRepository.findBySpacecraft(spacecraftId);
    }

    public List<Mission> findByLaunchDate(LocalDate launchDate) {
        return missionRepository.findByLaunchDate(launchDate);
    }

    public List<Mission> findByName(String name) {
        return missionRepository.findByName(name);
    }

}
