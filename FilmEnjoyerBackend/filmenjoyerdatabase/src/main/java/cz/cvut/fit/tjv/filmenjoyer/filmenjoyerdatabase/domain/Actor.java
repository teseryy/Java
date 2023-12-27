package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Actor implements EntityWithId<Long>{
    @Id
    @Column(name = "id_actor")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotBlank(message = "Name of actor is mandatory")
    private String name;
    @NotBlank(message = "Surname of actor is mandatory")
    private String surname;
    @Column(name = "date_of_birth")
    @Past(message = "The actor could not have been born in the future.")
    private Date dateOfBirth;

    @ManyToMany(mappedBy = "actors")
    private List<Film> films;

    public Actor(){}

    public Actor(Long id, String name, String surname, Date dateOfBirth, List<Film> films){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.films = films;
    }


    @Override
    public Long getId() {
        return id;
    }
}
