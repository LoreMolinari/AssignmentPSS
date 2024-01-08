package com.spaceagency.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@NoArgsConstructor
@Setter
@Getter
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer duration;
    @Column(nullable = false)
    private LocalDate launchDate;


    @OneToMany(mappedBy = "mission", fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JsonManagedReference
    @JsonIgnore
    private List<MissionLogs> missionLogs;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "mission_astronaut",
            joinColumns = @JoinColumn(name = "mission_id"),
            inverseJoinColumns = @JoinColumn(name = "astronaut_id")
    )
    @JsonIgnore
    private List<Astronaut> astronauts;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "spacecraft_id")
    @JsonIgnore
    private Spacecraft spacecraft;

    public Mission(String name, Integer duration, LocalDate launchDate) {
        this.name = name;
        this.duration = duration;
        this.launchDate = launchDate;
    }

    public Mission toView() {
        Mission view = new Mission();
        view.setId(this.id);
        view.setName(this.name);
        view.setDuration(this.duration);
        view.setLaunchDate(this.launchDate);
        view.setAstronauts(this.astronauts);
        view.setSpacecraft(this.spacecraft);

        if (this.missionLogs != null) {
            view.setMissionLogs(this.missionLogs.stream().map(MissionLogs::toView).collect(Collectors.toList()));
        }

        return view;
    }

    @Override
    public String toString() {
        return "Mission{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", durata=" + duration +
                ", launchDate=" + launchDate +
                ", astronauts=" + astronauts +
                ", spacecraft=" + spacecraft +
                ", missionLogs=" + missionLogs.stream().map(MissionLogs::toStringM).collect(Collectors.toList()) +
                '}';
    }


    public String toStringML() {
        return "Mission{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", durata=" + duration +
                ", launchDate=" + launchDate +
                ", astronauts=" + astronauts +
                ", spacecraft=" + spacecraft +
                '}';
    }


}
