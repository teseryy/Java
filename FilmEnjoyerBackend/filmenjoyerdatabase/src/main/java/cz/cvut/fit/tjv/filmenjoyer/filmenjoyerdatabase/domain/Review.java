package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Review implements EntityWithId<Long>{
    @Id
    @Column(name = "id_review")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "review_text")
    @NotBlank(message = "Text of review is mandatory")
    private String reviewText;
    @Positive(message = "Film rating is mandatory")
    @Max(value = 10, message = "Rating can't be more than 10.")
    private Integer rating;
    @Past(message = "The review could not have been written in the future.")
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_film")
    private Film film;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private User author;

    public Review(){}

    public Review(Long id, String reviewText, Integer rating, Date date, Film film, User author){
        this.id = id;
        this.reviewText = reviewText;
        this.rating = rating;
        this.date = date;
        this.film = film;
        this.author = author;
    }

    public Review(Long id, String reviewText, Integer rating, Date date){
        this.id = id;
        this.reviewText = reviewText;
        this.rating = rating;
        this.date = date;
    }

    @Override
    public Long getId() {
        return id;
    }
}
