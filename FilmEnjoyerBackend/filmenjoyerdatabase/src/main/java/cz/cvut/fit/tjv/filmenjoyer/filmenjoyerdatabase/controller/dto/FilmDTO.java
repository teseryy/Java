package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
public class FilmDTO {
    private final Long id;

    private final String title;

    private final Integer duration;

    private final String director;

    private final Date dateOfRelease;

    private final Integer budget;

    private final String country;

    private final List<Long> reviewIds;

    private final List<Long> actorIds;
}
