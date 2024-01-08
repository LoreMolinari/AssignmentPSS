package com.spaceagency.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class Spacecraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String spaceAgency;

    @OneToMany(mappedBy = "spacecraft", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Mission> missions;

    public Spacecraft(String name, String spaceagency) {
        this.name = name;
        this.spaceAgency = spaceagency;
    }


    public Spacecraft toView() {
        Spacecraft view = new Spacecraft();
        view.setId(this.id);
        view.setName(this.name);
        view.setSpaceAgency(this.spaceAgency);
        view.setMissions(this.missions);

        return view;
    }

    @Override
    public String toString() {
        return "Spacecraft{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", agenzia spaziale='" + spaceAgency + '\'' +
                "}";
    }
}
