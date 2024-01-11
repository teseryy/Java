package cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FilmDTO {

    private Long id;

    private String title;

    private Integer duration;

    private String director;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfRelease;

    private Integer budget;

    private String country;

    private List<Long> reviewIds;

    private List<Long> actorIds;
}