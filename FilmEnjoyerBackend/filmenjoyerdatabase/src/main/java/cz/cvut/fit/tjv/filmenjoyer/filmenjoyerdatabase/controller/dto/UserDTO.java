package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserDTO {
    private final String username;

    private final String name;

    private final String surname;

    private final List<Long> reviewIds;
}
