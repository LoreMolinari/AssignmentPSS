package com.spaceagency.repository;

import com.spaceagency.model.Mission;
import com.spaceagency.model.MissionLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MissionLogsRepository extends JpaRepository<MissionLogs, Long> {

    @Query("SELECT ml FROM MissionLogs ml WHERE ml.mission.id = :missionId")
    List<MissionLogs> findByMissionId(@Param("missionId") Long missionId);

    @Query("SELECT ml.mission FROM MissionLogs ml WHERE ml.id = :missionLogId")
    Optional<Mission> findMissionByMissionLogId(@Param("missionLogId") Long missionLogId);

    @Query("SELECT ml FROM MissionLogs ml WHERE ml.id = :missionLogId")
    MissionLogs findByMissionLogId(@Param("missionLogId") Long missionLogId);

}
