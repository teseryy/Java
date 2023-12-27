package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.application;

import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Film;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Review;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.persistent.FilmRepository;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.persistent.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class FilmServiceImpl extends CrudServiceImpl<Film, Long> implements FilmServiceInterface{
    private FilmRepository filmRepository;
    private ReviewRepository reviewRepository;

    @Override
    public List<Film> getFilmsWithReviewRating8AndMore() throws NoSuchElementException{
        if (!filmRepository.findAll().iterator().hasNext()) {
            throw new NoSuchElementException("No films available.");
        }

        return filmRepository.findFilmsWithReviewRating8AndMore();
    }

    @Override
    public void addReviewToFilm(Long filmId, Long reviewId) throws IllegalArgumentException {
        Optional<Film> optFilm = filmRepository.findById(filmId);
        Optional<Review> optReview = reviewRepository.findById(reviewId);
        if (optFilm.isEmpty()){
            throw new IllegalArgumentException("There's no such film with that id.");
        }

        if (optReview.isEmpty()){
            throw new IllegalArgumentException("There's no such review with that id.");
        }

        Film film = optFilm.get();
        Review review = optReview.get();

        film.getReviews().add(review);
        review.setFilm(film);

        filmRepository.save(film);
        reviewRepository.save(review);
    }

    @Override
    public void removeReviewFromFilm(Long filmId, Long reviewId) throws IllegalArgumentException {
        Optional<Film> optFilm = filmRepository.findById(filmId);
        Optional<Review> optReview = reviewRepository.findById(reviewId);
        if (optFilm.isEmpty()){
            throw new IllegalArgumentException("There's no such film with that id.");
        }

        if (optReview.isEmpty()){
            throw new IllegalArgumentException("There's no such review with that id.");
        }

        Film film = optFilm.get();
        Review review = optReview.get();

        if (!review.getFilm().equals(film)){
            throw new IllegalArgumentException("The film does not have a review with this id.");
        }

        film.getReviews().remove(review);
        review.setFilm(null);

        filmRepository.save(film);
        reviewRepository.save(review);
    }

    @Override
    protected CrudRepository<Film, Long> getRepository() {
        return filmRepository;
    }
}
