package com.spaceagency.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Astronaut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JsonProperty("name")
    private String name;

    @Column(nullable = false)
    @JsonProperty("surname")
    private String surname;

    @Column(nullable = false)
    @JsonProperty("nationality")
    private String nationality;

    @Column(nullable = false)
    @JsonProperty("birthyear")
    private Integer birthyear;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "collaboration",
            joinColumns = @JoinColumn(name = "astronaut_id"),
            inverseJoinColumns = @JoinColumn(name = "collaborator_id")
    )
    @JsonBackReference
    private List<Astronaut> collaborators;

    @ManyToMany(mappedBy = "astronauts", cascade = CascadeType.DETACH)
    @JsonIgnoreProperties("missions")
    @JsonManagedReference
    private List<Mission> missions;

    @OneToOne(mappedBy = "astronaut")
    @JsonIgnore
    private Spacesuit spacesuit;

    public Astronaut(String name, String surname, Integer birthyear, String nationality) {
        this.name = name;
        this.surname = surname;
        this.birthyear = birthyear;
        this.nationality = nationality;
    }

    public Astronaut toView() {
        Astronaut view = new Astronaut();
        view.setId(this.id);
        view.setName(this.name);
        view.setSurname(this.surname);
        view.setBirthyear(this.birthyear);
        view.setNationality(this.nationality);
        view.setMissions(this.missions);
        view.setCollaborators(this.collaborators);
        view.setSpacesuit(this.spacesuit);

        return view;
    }

    @Override
    public String toString() {
        return "Astronaut{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthyear=" + birthyear +
                ", nationality='" + nationality + '\'' +
                ", spacesuit=" + (spacesuit != null ? spacesuit.toString() : "null") +
                '}';
    }

}