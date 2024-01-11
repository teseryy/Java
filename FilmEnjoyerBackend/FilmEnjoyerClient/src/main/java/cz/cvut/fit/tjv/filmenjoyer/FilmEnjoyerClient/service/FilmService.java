package cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.service;

import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.api_client.FilmClient;
import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.model.ActorDTO;
import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.model.FilmDTO;
import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.model.ReviewDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class FilmService {
    private FilmClient filmClient;
    private Long currentFilm;

    FilmService(FilmClient filmClient){
        this.filmClient = filmClient;
    }

    public void setCurrentFilm(Long filmId){
        currentFilm = filmId;
        filmClient.setCurrentFilm(filmId);
    }

    public List<FilmDTO> readAll(){
        return filmClient.readAll();
    }

    public Optional<FilmDTO> read(){
        return filmClient.read();
    }

    public void create(FilmDTO data){
        filmClient.create(data);
    }

    public void delete(){
        filmClient.delete();
    }

    public List<ReviewDTO> getReviews(){
        return filmClient.getReviews();
    }

    public void createReviewForCurrentFilm(ReviewDTO reviewDTO){
        filmClient.createReviewForCurrentFilm(reviewDTO);
    }

    public void deleteReview(Long reviewId){
        filmClient.deleteReview(reviewId);
    }

    public void update(FilmDTO data){
        filmClient.update(data);
    }

    public List<FilmDTO> getFilmsWithReviewRating8AndMore(){
        return filmClient.getFilmsWithReviewRating8AndMore();
    }

    public List<ActorDTO> showFilmActor(){
        return filmClient.showFilmActors();
    }
}