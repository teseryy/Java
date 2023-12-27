package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.application;

import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Actor;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Film;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.persistent.ActorRepository;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.persistent.FilmRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ActorServiceImplIntegrationTest {
    @Autowired
    ActorServiceImpl actorService;
    @Autowired
    ActorRepository actorRepository;
    @Autowired
    FilmRepository filmRepository;
    Actor actor;
    Film film;

    @BeforeEach
    void setUp(){
        actorRepository.deleteAll();
        filmRepository.deleteAll();
        actor = new Actor(2L, "testName", "testSurname", new Date(103, 10, 30), new ArrayList<>());
        film = new Film(5L, "testTitle", 160, "Nolan", new Date(123, 10, 30), 100, "USA", new ArrayList<>(), new ArrayList<>());
        actorRepository.save(actor);
        filmRepository.save(film);
    }

    @Transactional
    @Test
    void addActorToFilm(){
        actorService.addActorToFilm(actor.getId(), film.getId());

        Actor actorFromDb = actorRepository.findById(actor.getId()).get();
        Assertions.assertTrue(actorFromDb.getFilms().contains(film));
        Assertions.assertEquals(1, actorFromDb.getFilms().size());
    }

    @Transactional
    @Test
    void removeActorFromFilm(){
        actorService.addActorToFilm(actor.getId(), film.getId());
        actorService.removeActorFromFilm(actor.getId(), film.getId());

        Actor actorFromDb = actorRepository.findById(actor.getId()).get();
        Assertions.assertFalse(actorFromDb.getFilms().contains(film));
        Assertions.assertEquals(0, actorFromDb.getFilms().size());
    }
}