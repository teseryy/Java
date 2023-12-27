package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.application;

import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Actor;

public interface ActorServiceInterface extends CrudService<Actor, Long> {
    public void addActorToFilm(Long actorId, Long filmId) throws IllegalArgumentException;

    public void removeActorFromFilm(Long actorId, Long filmId) throws IllegalArgumentException;
}
