package cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.api_client;

import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.model.ActorDTO;
import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.model.FilmDTO;
import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.model.ReviewDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.*;

@Component
public class FilmClient {
    RestClient filmClient;
    RestClient currentFilmClient;
    String baseUrl;
    ReviewClient reviewClient;
    UserClient userClient;
    ActorClient actorClient;

    FilmClient(@Value("${api.url}") String baseUrl, ReviewClient reviewClient, UserClient userClient){
        this.baseUrl = baseUrl;
        filmClient = RestClient.create(baseUrl + "/film");
        this.reviewClient = reviewClient;
        this.userClient = userClient;
    }

    public void setCurrentFilm(Long id){
        currentFilmClient = RestClient.builder()
                .baseUrl(baseUrl + "/film/{id}")
                .defaultUriVariables(Map.of("id", id))
                .build();
    }

    public List<FilmDTO> readAll(){
        return Arrays.asList(filmClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(FilmDTO[].class)
                .getBody());
    }

    public Optional<FilmDTO> read(){
        try {
            return Optional.of(currentFilmClient.get()
                    .retrieve().toEntity(FilmDTO.class).getBody());
        }catch (HttpClientErrorException.NotFound e){ //RestClientResponseException
            return Optional.empty();
        }
    }

    public void create (FilmDTO filmDTO){
        filmClient.post()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(filmDTO)
                .retrieve()
                .toBodilessEntity();
    }

    public List<ReviewDTO> getReviews(){
        List<ReviewDTO> reviews = new ArrayList<>();
        List<Long> reviewIds = currentFilmClient.get().retrieve().toEntity(FilmDTO.class).getBody().getReviewIds();
        for (Long id : reviewIds){
            reviewClient.setCurrentReview(id);
            reviews.add(reviewClient.read().get());
        }
        return reviews;
    }

    public void delete(){
        FilmDTO filmDTO = currentFilmClient.get().retrieve().toEntity(FilmDTO.class).getBody();
        List<Long> actorIds = filmDTO.getActorIds();

        for (Long id : actorIds){
            actorClient.setCurrentActor(id);
            actorClient.deleteFilmFromActor(filmDTO.getId());
        }

        for (Long reviewId : filmDTO.getReviewIds()){
            this.deleteReview(reviewId);
        }

        currentFilmClient.delete()
                .retrieve()
                .toBodilessEntity();
    }

    public void createReviewForCurrentFilm(ReviewDTO reviewDTO){
        FilmDTO film = currentFilmClient.get().retrieve().toEntity(FilmDTO.class).getBody();

        if (film.getDateOfRelease().after(new Date())){ // Komplexni dotaz
            throw new IllegalArgumentException();
        }

        Long reviewId = reviewClient.create(reviewDTO);
        String username = reviewDTO.getAuthor();
        currentFilmClient = RestClient.builder()
                .baseUrl(baseUrl + "/film/{id}/reviews?reviewId={reviewId}")
                .defaultUriVariables(Map.of("id", film.getId(), "reviewId", reviewId))
                .build();

        currentFilmClient.post()
                .retrieve()
                .toBodilessEntity();

        userClient.addReviewToUser(reviewId, username);
    }

    public void deleteReview(Long reviewId){
        reviewClient.setCurrentReview(reviewId);
        String author = reviewClient.read().get().getAuthor();
        Long filmId = currentFilmClient.get().retrieve().toEntity(FilmDTO.class).getBody().getId();

        currentFilmClient = RestClient.builder()
                .baseUrl(baseUrl + "/film/{id}/reviews/{reviewId}")
                .defaultUriVariables(Map.of("id", filmId, "reviewId", reviewId))
                .build();

        currentFilmClient.delete()
                .retrieve()
                .toBodilessEntity();

        userClient.deleteReviewFromUser(reviewId, author);

        reviewClient.setCurrentReview(reviewId);
        reviewClient.delete();
    }

    public List<ActorDTO> showFilmActors(){
        Long filmId = currentFilmClient.get().retrieve().toEntity(FilmDTO.class).getBody().getId();

        currentFilmClient = RestClient.builder()
                .baseUrl(baseUrl + "/film/{id}/actors")
                .defaultUriVariables(Map.of("id", filmId))
                .build();

        return Arrays.asList(currentFilmClient.get()
                .retrieve()
                .toEntity(ActorDTO[].class)
                .getBody());
    }

    public void update(FilmDTO filmDTO){
        currentFilmClient.put()
                .contentType(MediaType.APPLICATION_JSON)
                .body(filmDTO)
                .retrieve()
                .toBodilessEntity();
    }

    public List<FilmDTO> getFilmsWithReviewRating8AndMore(){
        RestClient newFilmClient =  RestClient.create(baseUrl + "/film/rating");

        return Arrays.asList(newFilmClient.get()
                .retrieve()
                .toEntity(FilmDTO[].class)
                .getBody());
    }
}
