package cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private String username;

    private String name;

    private String surname;

    private List<Long> reviewIds;
}