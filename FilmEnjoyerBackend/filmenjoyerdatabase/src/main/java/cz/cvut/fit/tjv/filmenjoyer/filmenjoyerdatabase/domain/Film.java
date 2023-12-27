package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Film implements EntityWithId<Long>{
    @Id
    @Column(name = "id_film")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotBlank(message = "Title is mandatory")
    private String title;
    @Positive(message = "Duration is mandatory")
    private Integer duration;
    @NotBlank(message = "Director is mandatory")
    private String director;
    @Column(name = "date_of_release")
    @Past(message = "The movie could not have been released in the future.")
    private Date dateOfRelease;
    @Positive(message = "Budget is mandatory")
    private Integer budget;
    @NotBlank(message = "Country of origin is mandatory")
    private String country;

    @OneToMany(targetEntity = Review.class, mappedBy = "film", fetch = FetchType.LAZY)
    private List<Review> reviews;
    @ManyToMany(targetEntity = Actor.class)
    @JoinTable(
            name = "filming",
            joinColumns = @JoinColumn(name = "id_actor"),
            inverseJoinColumns = @JoinColumn(name = "id_film")
    )
    private List<Actor> actors;

    public Film(){}

    public Film(Long id, String title, Integer duration, String director, Date dateOfRelease,
                Integer budget, String country, List<Review> reviews, List<Actor> actors){
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.director = director;
        this.dateOfRelease = dateOfRelease;
        this.budget = budget;
        this.country = country;
        this.reviews = reviews;
        this.actors = actors;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Film f){
            return id == f.id;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public Long getId() {
        return id;
    }
}
