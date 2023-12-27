package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.application;

import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Film;

import java.util.List;
import java.util.NoSuchElementException;

public interface FilmServiceInterface extends CrudService<Film, Long> {

    public List<Film> getFilmsWithReviewRating8AndMore() throws NoSuchElementException;

    public void addReviewToFilm(Long filmId, Long reviewId) throws IllegalArgumentException;

    public void removeReviewFromFilm(Long filmId, Long reviewId) throws IllegalArgumentException;
}
