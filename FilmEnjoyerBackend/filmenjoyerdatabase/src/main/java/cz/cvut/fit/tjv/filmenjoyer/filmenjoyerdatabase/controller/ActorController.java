package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller;

import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.application.ActorServiceImpl;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.converter.DTOConverter;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.dto.ActorDTO;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.dto.FilmDTO;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Actor;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Film;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/rest/api/actor", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ActorController {
    private final ActorServiceImpl actorService;
    private final DTOConverter<ActorDTO, Actor> actorDTOConverter;
    private final DTOConverter<FilmDTO, Film> filmDTOConverter;

    @GetMapping
    public List<ActorDTO> getActors(){
        Iterable<Actor> actors = actorService.readAll();
        List<ActorDTO> actorDTOs = new ArrayList<>();

        for (Actor actor : actors) {
            actorDTOs.add(actorDTOConverter.toDTO(actor));
        }

        return actorDTOs;
    }

    @GetMapping(path = "/{id}")
    public ActorDTO getActor(@PathVariable("id") Long id){
        return actorDTOConverter.toDTO(actorService.readById(id).get());
    }

    @GetMapping(path = "/{id}/films")
    public List<FilmDTO> getActorFilms(@PathVariable("id") Long id){
        return actorService.readById(id).get().getFilms().stream().map(filmDTOConverter::toDTO).toList();
    }

    @PostMapping
    public ActorDTO createActor(@RequestBody ActorDTO a){
        return actorDTOConverter.toDTO(actorService.create(actorDTOConverter.toEntity(a)));
    }

    @PostMapping(path = "/{actorId}/films")
    public List<FilmDTO> addActorToFilm(@PathVariable("actorId") Long actorId, Long filmId){
        actorService.addActorToFilm(actorId, filmId);

        return actorService.readById(actorId).get().getFilms().stream().map(filmDTOConverter::toDTO).toList();
    }

    @DeleteMapping(path = "/{actorId}/films/{filmId}")
    public List<FilmDTO> removeActorFromFilm(@PathVariable("actorId") Long actorId, @PathVariable("filmId") Long filmId){
        actorService.removeActorFromFilm(actorId, filmId);

        return actorService.readById(actorId).get().getFilms().stream().map(filmDTOConverter::toDTO).toList();
    }

    @PutMapping(path = "/{id}")
    public void updateActor(@PathVariable("id") Long id, @RequestBody ActorDTO a){
        Actor e = actorDTOConverter.toEntity(a);
        e.setId(id);
        actorService.update(id, e);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteActor(@PathVariable("id") Long id){
        actorService.deleteById(id);
    }
}
