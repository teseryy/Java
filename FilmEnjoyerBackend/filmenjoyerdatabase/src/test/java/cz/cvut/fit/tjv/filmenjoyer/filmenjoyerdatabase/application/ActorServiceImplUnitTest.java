package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.application;

import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Actor;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Film;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.persistent.ActorRepository;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.persistent.FilmRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ActorServiceImplUnitTest {
    @Autowired
    ActorServiceImpl actorService;
    @MockBean
    ActorRepository actorRepository;
    @MockBean
    FilmRepository filmRepository;
    Actor actor;
    Film film;

    @BeforeEach
    void setUp(){
        actor = new Actor(2L, "testName", "testSurname", new Date(103, 10, 30), new ArrayList<>());
        film = new Film(2L, "testTitle", 160, "Nolan", new Date(123, 10, 30), 100, "USA", new ArrayList<>(), new ArrayList<>());
        Mockito.when(
                actorRepository.findById(actor.getId())
        ).thenReturn(Optional.of(actor));
        Mockito.when(
                filmRepository.findById(film.getId())
        ).thenReturn(Optional.of(film));
    }

    @Test
    void addActorToFilm(){
        actorService.addActorToFilm(actor.getId(), film.getId());

        Assertions.assertTrue(actor.getFilms().contains(film));
        Assertions.assertTrue(film.getActors().contains(actor));

        Assertions.assertEquals(1, actor.getFilms().size());
        Assertions.assertEquals(1, film.getActors().size());

        Mockito.verify(filmRepository, Mockito.atLeastOnce()).save(film);
        Mockito.verify(actorRepository, Mockito.atLeastOnce()).save(actor);
    }

    @Test
    void addActorToFilmInvalidActorId(){
        Mockito.when(
                actorRepository.findById(actor.getId())
        ).thenReturn(Optional.empty());

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> actorService.addActorToFilm(actor.getId(), film.getId())
        );

        Mockito.verify(actorRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(filmRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void addActorToFilmInvalidFilmId(){
        Mockito.when(
                filmRepository.findById(film.getId())
        ).thenReturn(Optional.empty());

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> actorService.addActorToFilm(actor.getId(), film.getId())
        );

        Mockito.verify(actorRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(filmRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void removeActorFromFilm(){
        actorService.addActorToFilm(actor.getId(), film.getId());
        actorService.removeActorFromFilm(actor.getId(), film.getId());

        Assertions.assertFalse(actor.getFilms().contains(film));
        Assertions.assertFalse(film.getActors().contains(actor));

        Assertions.assertEquals(0, actor.getFilms().size());
        Assertions.assertEquals(0, film.getActors().size());

        Mockito.verify(filmRepository, Mockito.atLeast(2)).save(film);
        Mockito.verify(actorRepository, Mockito.atLeast(2)).save(actor);
    }

    @Test
    void removeActorFromFilmInvalidActorId(){
        actorService.addActorToFilm(actor.getId(), film.getId());

        Mockito.when(
                actorRepository.findById(actor.getId())
        ).thenReturn(Optional.empty());

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> actorService.removeActorFromFilm(actor.getId(), film.getId())
        );

        Mockito.verify(actorRepository, Mockito.atMostOnce()).save(actor);
        Mockito.verify(filmRepository, Mockito.atMostOnce()).save(film);
    }

    @Test
    void removeActorFromFilmInvalidFilmId(){
        actorService.addActorToFilm(actor.getId(), film.getId());

        Mockito.when(
                filmRepository.findById(film.getId())
        ).thenReturn(Optional.empty());

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> actorService.removeActorFromFilm(actor.getId(), film.getId())
        );

        Mockito.verify(actorRepository, Mockito.atMostOnce()).save(actor);
        Mockito.verify(filmRepository, Mockito.atMostOnce()).save(film);
    }
}