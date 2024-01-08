package com.spaceagency.service;

import com.spaceagency.model.Astronaut;
import com.spaceagency.model.Spacesuit;
import com.spaceagency.repository.SpacesuitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpacesuitService {

    private final SpacesuitRepository spacesuitRepository;

    @Autowired
    public SpacesuitService(SpacesuitRepository spacesuitRepository) {
        this.spacesuitRepository = spacesuitRepository;
    }


    public List<Spacesuit> getAllSpacesuits() {
        return spacesuitRepository.findAll();
    }

    public Spacesuit getSpacesuitById(Long id) {
        return spacesuitRepository.findById(id).orElse(null);
    }

    public List<Spacesuit> getSpacesuitByName(String name) {
        return spacesuitRepository.findByName(name);
    }

    public Spacesuit saveSpacesuit(Spacesuit spacesuit) {
        return spacesuitRepository.save(spacesuit);
    }

    public Spacesuit getSpacesuitsByAstronautId(Astronaut astronaut) {
        return spacesuitRepository.findByAstronautId(astronaut);
    }

    public void deleteSpacesuit(Long id) {
        spacesuitRepository.deleteById(id);
    }
}
