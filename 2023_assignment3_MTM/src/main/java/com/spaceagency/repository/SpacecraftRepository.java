package com.spaceagency.repository;

import com.spaceagency.model.Astronaut;
import com.spaceagency.model.Spacecraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpacecraftRepository extends JpaRepository<Spacecraft, Long> {

    @Query("SELECT s FROM Spacecraft s WHERE s.name = :name")
    List<Spacecraft> findByName(@Param("name") String name);

    @Query("SELECT s FROM Spacecraft s JOIN s.missions m WHERE :astronaut MEMBER OF m.astronauts")
    List<Spacecraft> findByAstronaut(@Param("astronaut") Astronaut astronaut);

    @Query("SELECT s FROM Spacecraft s WHERE s.spaceAgency = :spaceAgency")
    List<Spacecraft> findBySpaceAgency(@Param("spaceAgency") String spaceAgency);

    @Query("SELECT s FROM Spacecraft s JOIN s.missions m WHERE m.id = :missionId")
    List<Spacecraft> findByMissionId(@Param("missionId") Long missionId);
}
