package com.spaceagency.service;

import com.spaceagency.model.Mission;
import com.spaceagency.model.MissionLogs;
import com.spaceagency.repository.MissionLogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MissionLogsService {

    private final MissionLogsRepository missionLogsRepository;

    @Autowired
    public MissionLogsService(MissionLogsRepository missionLogsRepository) {
        this.missionLogsRepository = missionLogsRepository;
    }

    public List<MissionLogs> getAllMissionLogs() {
        return missionLogsRepository.findAll();
    }

    public MissionLogs getMissionLogById(Long missionLogId) {
        return missionLogsRepository.findByMissionLogId(missionLogId);
    }

    public List<MissionLogs> getMissionLogsByMissionId(Long missionId) {
        return missionLogsRepository.findByMissionId(missionId);
    }

    public MissionLogs saveMissionLogs(MissionLogs missionLogs) {
        return missionLogsRepository.save(missionLogs);
    }

    public void deleteMissionLogsById(Long id) {
        missionLogsRepository.deleteById(id);
    }

    public Optional<Mission> getMissionByMissionLogId(Long missionLogId) {
        return missionLogsRepository.findMissionByMissionLogId(missionLogId);
    }
}
