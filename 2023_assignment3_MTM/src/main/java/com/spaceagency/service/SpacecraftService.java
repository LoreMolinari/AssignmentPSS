package com.spaceagency.service;

import com.spaceagency.model.Astronaut;
import com.spaceagency.model.Mission;
import com.spaceagency.model.Spacecraft;
import com.spaceagency.repository.SpacecraftRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpacecraftService {

    private final SpacecraftRepository spacecraftRepository;

    @Autowired
    public SpacecraftService(SpacecraftRepository spacecraftRepository) {
        this.spacecraftRepository = spacecraftRepository;
    }

    public List<Spacecraft> getAllSpacecrafts() {
        return spacecraftRepository.findAll();
    }

    public Spacecraft getSpacecraftById(Long id) {
        return spacecraftRepository.findById(id).orElse(null);
    }

    public Spacecraft saveSpacecraft(Spacecraft spacecraft) {
        return spacecraftRepository.save(spacecraft);
    }

    public void deleteSpacecraft(Long spacecraftId) {
        Spacecraft spacecraft = spacecraftRepository.findById(spacecraftId).orElse(null);

        if (spacecraft != null) {
            for (Mission mission : spacecraft.getMissions()) {
                mission.getAstronauts().remove(spacecraft);
            }

            spacecraftRepository.save(spacecraft);
        }

        spacecraftRepository.delete(spacecraft);
    }

    public List<Spacecraft> findByName(String name) {
        return spacecraftRepository.findByName(name);
    }

    public List<Spacecraft> findBySpaceAgency(String spaceAgency) {
        return spacecraftRepository.findBySpaceAgency(spaceAgency);
    }

    public List<Spacecraft> findByAstronaut(Long astronautId) {
        Astronaut astronaut = new Astronaut();
        astronaut.setId(astronautId);
        return spacecraftRepository.findByAstronaut(astronaut);
    }

    public List<Spacecraft> findByMissionId(Mission mission) {
        List<Spacecraft> spacecrafts = spacecraftRepository.findByMissionId(mission.getId());
        for (Spacecraft spacecraft : spacecrafts) {
            Hibernate.initialize(spacecraft.getMissions());
        }
        return spacecrafts;
    }

}
