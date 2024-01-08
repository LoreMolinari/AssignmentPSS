package com.spaceagency.repository;

import com.spaceagency.model.Astronaut;
import com.spaceagency.model.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AstronautRepository extends JpaRepository<Astronaut, Long> {

    @Query(value = "SELECT * FROM astronaut WHERE name = ?1", nativeQuery = true)
    List<Astronaut> findByName(String name);

    @Query(value = "SELECT * FROM astronaut WHERE nationality = ?1", nativeQuery = true)
    List<Astronaut> findByCountry(String nationality);

    @Query(value = "SELECT * FROM astronaut WHERE birthyear = ?1", nativeQuery = true)
    List<Astronaut> findByYear(Integer birthyear);

    @Query("SELECT a FROM Astronaut a JOIN a.missions m WHERE :mission MEMBER OF a.missions")
    List<Astronaut> findByMission(@Param("mission") Mission mission);


}