package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.application;

import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Actor;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Film;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.persistent.ActorRepository;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.persistent.FilmRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class ActorServiceImpl extends CrudServiceImpl<Actor,Long> implements ActorServiceInterface {
    private ActorRepository actorRepository;
    private FilmRepository filmRepository;

    @Override
    public void addActorToFilm(Long actorId, Long filmId) throws IllegalArgumentException {
        Optional<Actor> optActor = actorRepository.findById(actorId);
        Optional<Film> optFilm = filmRepository.findById(filmId);
        if (optActor.isEmpty()){
            throw new IllegalArgumentException("There's no such actor with that id.");
        }

        if (optFilm.isEmpty()){
            throw new IllegalArgumentException("There's no such film with that id.");
        }

        Actor actor = optActor.get();
        Film film = optFilm.get();

        actor.getFilms().add(film);
        film.getActors().add(actor);

        actorRepository.save(actor);
        filmRepository.save(film);
    }

    @Override
    public void removeActorFromFilm(Long actorId, Long filmId) throws IllegalArgumentException {
        Optional<Actor> optActor = actorRepository.findById(actorId);
        Optional<Film> optFilm = filmRepository.findById(filmId);
        if (optActor.isEmpty()){
            throw new IllegalArgumentException("There's no such actor with that id.");
        }

        if (optFilm.isEmpty()){
            throw new IllegalArgumentException("There's no such film with that id.");
        }

        Actor actor = optActor.get();
        Film film = optFilm.get();

        if (!film.getActors().contains(actor)){
            throw new IllegalArgumentException("The actor is not appearing in this movie");
        }

        film.getActors().remove(actor);
        actor.getFilms().remove(film);

        actorRepository.save(actor);
        filmRepository.save(film);
    }

    @Override
    protected CrudRepository<Actor, Long> getRepository() {
        return actorRepository;
    }
}
