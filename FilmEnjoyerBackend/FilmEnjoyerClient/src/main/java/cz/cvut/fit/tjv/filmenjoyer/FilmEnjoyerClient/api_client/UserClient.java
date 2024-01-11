package cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.api_client;

import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.model.ReviewDTO;
import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.model.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class UserClient {
    private RestClient userClient;
    private String baseUrl;
    private RestClient currentUserClient;
    private ReviewClient reviewClient;

    public UserClient(@Value("${api.url}") String baseUrl, ReviewClient reviewClient){
        this.baseUrl = baseUrl;
        userClient = RestClient.create(baseUrl + "/user");
        this.reviewClient = reviewClient;
    }

    public void setCurrentUser(String username){
        currentUserClient = RestClient.builder()
                .baseUrl(baseUrl + "/user/{id}")
                .defaultUriVariables(Map.of("id", username))
                .build();
    }

    public List<UserDTO> readAll(){
        return Arrays.asList(userClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(UserDTO[].class)
                .getBody());
    }

    public Optional<UserDTO> read(){
        try {
            return Optional.of(currentUserClient.get()
                    .retrieve().toEntity(UserDTO.class).getBody());
        }catch (HttpClientErrorException.NotFound e){ //RestClientResponseException
            return Optional.empty();
        }
    }

    public void create (UserDTO userDTO){
        userClient.post()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userDTO)
                .retrieve()
                .toBodilessEntity();
    }

    public void delete(){
        String username = currentUserClient.get().retrieve().toEntity(UserDTO.class).getBody().getUsername();

        List<ReviewDTO> reviews = this.getUserReviews();
        for (ReviewDTO review : reviews){
            RestClient filmClient = RestClient.builder()
                    .baseUrl(baseUrl + "/film/{id}/reviews/{reviewId}")
                    .defaultUriVariables(Map.of("id", review.getFilmId(), "reviewId", review.getId()))
                    .build();

            filmClient.delete()
                    .retrieve()
                    .toBodilessEntity();

            reviewClient.setCurrentReview(review.getId());
            reviewClient.delete();
        }

        currentUserClient = RestClient.builder()
                .baseUrl(baseUrl + "/user/{id}")
                .defaultUriVariables(Map.of("id", username))
                .build();

        currentUserClient.delete()
                .retrieve()
                .toBodilessEntity();
    }

    public void update(UserDTO userDTO){
        currentUserClient.put()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userDTO)
                .retrieve()
                .toBodilessEntity();
    }

    public void addReviewToUser(Long reviewId, String username){
        currentUserClient = RestClient.builder()
                .baseUrl(baseUrl + "/user/{id}/reviews?reviewId={reviewId}")
                .defaultUriVariables(Map.of("id", username, "reviewId", reviewId))
                .build();

        currentUserClient.post()
                .retrieve()
                .toBodilessEntity();
    }

    public void deleteReviewFromUser(Long reviewId, String username){
        currentUserClient = RestClient.builder()
                .baseUrl(baseUrl + "/user/{id}/reviews/{reviewId}")
                .defaultUriVariables(Map.of("id", username, "reviewId", reviewId))
                .build();

        currentUserClient.delete()
                .retrieve()
                .toBodilessEntity();
    }

    public List<ReviewDTO> getUserReviews(){
        String username = currentUserClient.get().retrieve().toEntity(UserDTO.class).getBody().getUsername();

        currentUserClient = RestClient.builder()
                .baseUrl(baseUrl + "/user/{id}/reviews")
                .defaultUriVariables(Map.of("id", username))
                .build();

        return Arrays.asList(currentUserClient.get()
                .retrieve()
                .toEntity(ReviewDTO[].class)
                .getBody());
    }
}
