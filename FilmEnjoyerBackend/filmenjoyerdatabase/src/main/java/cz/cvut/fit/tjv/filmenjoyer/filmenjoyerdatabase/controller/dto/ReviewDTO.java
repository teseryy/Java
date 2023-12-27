package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class ReviewDTO {
    private final Long id;

    private final String reviewText;

    private final Integer rating;

    private final Date date;

    private final Long filmId;

    private final String author;
}
