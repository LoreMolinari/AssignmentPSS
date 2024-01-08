package com.spaceagency.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Spacesuit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String model;

    @OneToOne
    @JoinColumn(name = "astronaut_id")
    private Astronaut astronaut;

    public Spacesuit(String model) {
        this.model = model;
    }

    public Spacesuit toView() {
        Spacesuit view = new Spacesuit();
        view.setId(this.id);
        view.setModel(this.model);
        view.setAstronaut(this.astronaut);

        return view;
    }

    @Override
    public String toString() {
        return "Spacesuit{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", astronaut=" + (astronaut != null ? " " + astronaut.getId() + " - " + astronaut.getName() + " " + astronaut.getSurname() : "null") +
                '}';
    }

}
