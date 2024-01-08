package com.spaceagency.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class MissionLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String logMessage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mission_id")
    private Mission mission;

    public MissionLogs(String logMessage, Mission mission) {
        this.logMessage = logMessage;
        this.mission = mission;
    }

    public MissionLogs toView() {
        MissionLogs missionLogs = new MissionLogs();
        missionLogs.setId(this.id);
        missionLogs.setLogMessage(this.logMessage);

        if (this.mission != null) {
            Mission missionEntity = this.mission;
            missionLogs.setMission(missionEntity);
        }

        return missionLogs;
    }

    @Override
    public String toString() {
        return "MissionLogs{" +
                "id=" + id +
                ", logMessage='" + logMessage + '\'' +
                ", mission=" + (mission != null ? mission.toStringML() : "null") +
                '}';
    }

    public String toStringM() {
        return "MissionLogs{" +
                "id=" + id +
                ", logMessage='" + logMessage + '\'' +
                '}';
    }

}
