package com.spaceagency.repository;

import com.spaceagency.model.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    @Query("SELECT DISTINCT m FROM Mission m " +
            "LEFT JOIN FETCH m.astronauts a " +
            "WHERE a.id = :astronautId")
    List<Mission> findByAstronaut(@Param("astronautId") Long astronautId);

    @Query("SELECT DISTINCT m FROM Mission m " +
            "LEFT JOIN FETCH m.spacecraft " +
            "WHERE m.spacecraft.id = :spacecraftId")
    List<Mission> findBySpacecraft(@Param("spacecraftId") Long spacecraftId);

    @Query("SELECT DISTINCT m FROM Mission m " +
            "LEFT JOIN FETCH m.missionLogs " +
            "WHERE m.launchDate = :launchDate")
    List<Mission> findByLaunchDate(@Param("launchDate") LocalDate launchDate);

    @Query("SELECT DISTINCT m FROM Mission m " +
            "LEFT JOIN FETCH m.missionLogs " +
            "WHERE m.name = :name")
    List<Mission> findByName(@Param("name") String name);
}
