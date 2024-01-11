package cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ReviewDTO {
    private Long id;

    private String reviewText;

    private Integer rating;

    private Date date;

    private Long filmId;

    private String author;
}