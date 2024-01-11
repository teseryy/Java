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
public class ActorDTO {
    private Long id;

    private String name;

    private String surname;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    private List<Long> filmIDs;
}