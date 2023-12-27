package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller;

import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.application.FilmServiceImpl;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.application.ReviewServiceImpl;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.converter.DTOConverter;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.dto.ActorDTO;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.dto.FilmDTO;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.dto.ReviewDTO;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Actor;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Film;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Review;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/rest/api/film", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class FilmController {
    private final FilmServiceImpl filmService;
    private final DTOConverter<FilmDTO, Film> filmDTOConverter;
    private final DTOConverter<ActorDTO, Actor> actorDTOConverter;
    private final DTOConverter<ReviewDTO, Review> reviewDTOConverter;
    private final ReviewServiceImpl reviewService;

    @GetMapping
    public List<FilmDTO> getFilms() {
        Iterable<Film> films = filmService.readAll();
        List<FilmDTO> filmDTOs = new ArrayList<>();

        for (Film film : films) {
            filmDTOs.add(filmDTOConverter.toDTO(film));
        }

        return filmDTOs;
    }

    @GetMapping(path = "/{id}")
    public FilmDTO getFilm(@PathVariable("id") Long id){
        return filmDTOConverter.toDTO(filmService.readById(id).get());
    }

    @GetMapping(path = "/{id}/actors")
    public List<ActorDTO> getFilmActors(@PathVariable("id") Long id){
        return filmService.readById(id).get().getActors().stream().map(actorDTOConverter::toDTO).toList();
    }

    @GetMapping(path = "/{id}/reviews")
    public List<ReviewDTO> getFilmReviews(@PathVariable("id") Long id){
        return filmService.readById(id).get().getReviews().stream().map(reviewDTOConverter::toDTO).toList();
    }

    @PostMapping
    public FilmDTO createFilm(@RequestBody FilmDTO filmDTO){
        return filmDTOConverter.toDTO(filmService.create(filmDTOConverter.toEntity(filmDTO)));
    }

    @PutMapping(path = "/{id}")
    public void updateFilm(@PathVariable("id") Long id, @RequestBody FilmDTO filmDTO){
        Film film = filmDTOConverter.toEntity(filmDTO);
        film.setId(id);
        filmService.update(id, film);
    }

    @Operation(description = "Returns movies that have reviews from users with a rating of 8 or higher.")
    @GetMapping("/rating")
    public List<FilmDTO> getFilmsWithReviewRating8AndMore(){
        return filmService.getFilmsWithReviewRating8AndMore().stream().map(filmDTOConverter::toDTO).toList();
    }

    @PostMapping(path = "/{id}/reviews")
    public List<ReviewDTO> addReviewToFilm(@PathVariable("id") Long filmId, Long reviewId){
        Review review = reviewService.readById(reviewId).get();
        Film film = filmService.readById(filmId).get();

        if (film.getDateOfRelease().after(review.getDate())){
            throw new ReviewDateException("A review can't be written before the movie is released.");
        }

        filmService.addReviewToFilm(filmId, reviewId);

        return filmService.readById(filmId).get().getReviews().stream().map(reviewDTOConverter::toDTO).toList();
    }

    @DeleteMapping(path = "/{filmId}/reviews/{reviewId}")
    public List<ReviewDTO> removeReviewFromFilm(@PathVariable("filmId") Long filmId, @PathVariable("reviewId") Long reviewId){
        filmService.removeReviewFromFilm(filmId, reviewId);

        return filmService.readById(filmId).get().getReviews().stream().map(reviewDTOConverter::toDTO).toList();
    }

    @DeleteMapping(path = "/{id}")
    public void deleteFilm(@PathVariable("id") Long id){
        filmService.deleteById(id);
    }
}
