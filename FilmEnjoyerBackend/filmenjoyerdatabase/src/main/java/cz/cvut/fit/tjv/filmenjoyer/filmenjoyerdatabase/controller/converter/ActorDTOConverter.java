package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.converter;

import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.application.FilmServiceImpl;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.dto.ActorDTO;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Actor;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Film;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ActorDTOConverter implements DTOConverter<ActorDTO, Actor> {
    private final FilmServiceImpl filmService;

    @Override
    public ActorDTO toDTO(Actor actor){
        return new ActorDTO(actor.getId(), actor.getName(), actor.getSurname(),
                            actor.getDateOfBirth(), actor.getFilms().stream().map(Film::getId).toList());
    }

    @Override
    public Actor toEntity(ActorDTO actorDTO){
        List<Film> films = new ArrayList<>();
        Optional<List<Long>> filmsActor = Optional.ofNullable(actorDTO.getFilmIDs());
        if (filmsActor.isPresent()) {
            for (Long filmId : actorDTO.getFilmIDs()) {
                Optional<Film> optionalFilm = filmService.readById(filmId);
                optionalFilm.ifPresent(films::add);
            }
        }


        return new Actor(actorDTO.getId(), actorDTO.getName(), actorDTO.getSurname(), actorDTO.getDateOfBirth(), films);
    }
}
