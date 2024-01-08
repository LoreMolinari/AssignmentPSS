package com.spaceagency.service;

import com.spaceagency.model.Astronaut;
import com.spaceagency.model.Mission;
import com.spaceagency.repository.AstronautRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AstronautService {

    private final AstronautRepository astronautRepository;

    @Autowired
    public AstronautService(AstronautRepository astronautRepository) {
        this.astronautRepository = astronautRepository;
    }

    public List<Astronaut> getAllAstronauts() {
        return astronautRepository.findAll();
    }

    public Astronaut getAstronautById(Long id) {
        return astronautRepository.findById(id).orElse(null);
    }

    public Astronaut saveAstronaut(Astronaut astronaut) {
        return astronautRepository.save(astronaut);
    }


    public void deleteAstronaut(Long astronautId) {
        Astronaut astronaut = astronautRepository.findById(astronautId).orElse(null);

        if (astronaut != null) {
            for (Mission mission : astronaut.getMissions()) {
                mission.getAstronauts().remove(astronaut);
            }

            for (Astronaut collaborator : astronaut.getCollaborators()) {
                collaborator.getCollaborators().remove(astronaut);
            }

            if (astronaut.getSpacesuit() != null) {
                astronaut.getSpacesuit().setAstronaut(null);
            }

            astronautRepository.save(astronaut);
        }

        astronautRepository.delete(astronaut);
    }


    public List<Astronaut> findByName(String name) {
        return astronautRepository.findByName(name);
    }

    public List<Astronaut> findByCountry(String nationality) {
        return astronautRepository.findByCountry(nationality);
    }

    public List<Astronaut> findByYear(String year) {
        return astronautRepository.findByYear(Integer.valueOf(year));
    }

    public List<Astronaut> findByMission(Mission mission) {
        List<Astronaut> astronauts = astronautRepository.findByMission(mission);
        for (Astronaut astronaut : astronauts) {
            Hibernate.initialize(astronaut.getMissions());
        }
        return astronauts;
    }

    public List<Astronaut> getCollaborators(Long astronautId) {
        Astronaut astronaut = getAstronautById(astronautId);
        return astronaut.getCollaborators();
    }
}