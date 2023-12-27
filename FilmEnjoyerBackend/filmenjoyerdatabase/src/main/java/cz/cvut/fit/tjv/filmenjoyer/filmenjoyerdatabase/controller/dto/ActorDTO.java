package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
public class ActorDTO {
    private final Long id;

    private final String name;

    private final String surname;

    private final Date dateOfBirth;

    private final List<Long> filmIDs;
}
